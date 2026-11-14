package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.ClienteDTO;
import ec.edu.espe.gimnasio_api.entity.Cliente;
import ec.edu.espe.gimnasio_api.entity.Entrenador;
import ec.edu.espe.gimnasio_api.entity.Plan;
import ec.edu.espe.gimnasio_api.exception.RecursoNoEncontradoException;
import ec.edu.espe.gimnasio_api.exception.ReglaNegocioException;
import ec.edu.espe.gimnasio_api.repository.ClienteRepository;
import ec.edu.espe.gimnasio_api.repository.EntrenadorRepository;
import ec.edu.espe.gimnasio_api.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PlanRepository planRepository;
    private final EntrenadorRepository entrenadorRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              PlanRepository planRepository,
                              EntrenadorRepository entrenadorRepository) {
        this.clienteRepository = clienteRepository;
        this.planRepository = planRepository;
        this.entrenadorRepository = entrenadorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::aDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerPorId(Long id) {
        return aDTO(buscarCliente(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarMembresiasVencidas() {
        // Vencida = la fecha de vencimiento ya pasó (hoy todavía es válida)
        return clienteRepository.findByFechaVencimientoMembresiaBefore(LocalDate.now()).stream()
                .map(this::aDTO)
                .toList();
    }

    @Override
    public ClienteDTO registrar(ClienteDTO dto) {
        String cedula = dto.getCedula().trim();
        String email = dto.getEmail().trim().toLowerCase();

        validarCedulaEcuatoriana(cedula);
        if (clienteRepository.existsByCedula(cedula)) {
            throw new ReglaNegocioException("Ya existe un cliente registrado con la cédula " + cedula);
        }
        if (clienteRepository.existsByEmail(email)) {
            throw new ReglaNegocioException("Ya existe un cliente registrado con el email " + email);
        }

        Plan plan = buscarPlan(dto.getPlanId());
        validarPlanActivo(plan);

        Cliente cliente = new Cliente();
        cliente.setCedula(cedula);
        cliente.setNombre(dto.getNombre().trim());
        cliente.setEmail(email);
        cliente.setPlan(plan);
        cliente.setEntrenador(buscarEntrenadorOpcional(dto.getEntrenadorId()));

        // Regla: si no se envía la fecha, la membresía empieza hoy y dura lo que indica el plan
        if (dto.getFechaVencimientoMembresia() != null) {
            validarFechaNoPasada(dto.getFechaVencimientoMembresia());
            cliente.setFechaVencimientoMembresia(dto.getFechaVencimientoMembresia());
        } else {
            cliente.setFechaVencimientoMembresia(calcularVencimiento(plan));
        }

        return aDTO(clienteRepository.save(cliente));
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarCliente(id);
        String cedula = dto.getCedula().trim();
        String email = dto.getEmail().trim().toLowerCase();

        validarCedulaEcuatoriana(cedula);
        if (clienteRepository.existsByCedulaAndIdNot(cedula, id)) {
            throw new ReglaNegocioException("La cédula " + cedula + " ya pertenece a otro cliente");
        }
        if (clienteRepository.existsByEmailAndIdNot(email, id)) {
            throw new ReglaNegocioException("El email " + email + " ya pertenece a otro cliente");
        }

        Plan planNuevo = buscarPlan(dto.getPlanId());
        boolean cambioDePlan = !Objects.equals(cliente.getPlan().getId(), planNuevo.getId());
        if (cambioDePlan) {
            validarPlanActivo(planNuevo);
        }

        cliente.setCedula(cedula);
        cliente.setNombre(dto.getNombre().trim());
        cliente.setEmail(email);
        cliente.setPlan(planNuevo);
        cliente.setEntrenador(buscarEntrenadorOpcional(dto.getEntrenadorId()));

        // Regla: fecha enviada > se respeta; cambio de plan sin fecha > se recalcula desde hoy;
        // en otro caso se mantiene la fecha actual
        if (dto.getFechaVencimientoMembresia() != null) {
            cliente.setFechaVencimientoMembresia(dto.getFechaVencimientoMembresia());
        } else if (cambioDePlan) {
            cliente.setFechaVencimientoMembresia(calcularVencimiento(planNuevo));
        }

        return aDTO(clienteRepository.save(cliente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = buscarCliente(id);
        // Las asistencias del cliente se eliminan en cascada (CascadeType.REMOVE en la entidad Cliente)
        clienteRepository.delete(cliente);
    }

    // ---------- Métodos de apoyo ----------

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + id));
    }

    private Plan buscarPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un plan con id " + planId));
    }

    private Entrenador buscarEntrenadorOpcional(Long entrenadorId) {
        if (entrenadorId == null) {
            return null;
        }
        return entrenadorRepository.findById(entrenadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un entrenador con id " + entrenadorId));
    }

    private void validarPlanActivo(Plan plan) {
        if (!Boolean.TRUE.equals(plan.getActivo())) {
            throw new ReglaNegocioException("El plan '" + plan.getNombre() + "' no está activo y no admite nuevas inscripciones");
        }
    }

    private LocalDate calcularVencimiento(Plan plan) {
        return LocalDate.now().plusDays(plan.getDuracionDias());
    }

    private void validarFechaNoPasada(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new ReglaNegocioException("No se puede registrar un cliente con la membresía ya vencida");
        }
    }

    /**
     * Regla de dominio: la cédula debe ser ecuatoriana válida.
     * - 10 dígitos
     * - Los 2 primeros = provincia (01 a 24, o 30 para ecuatorianos en el exterior)
     * - El tercer dígito menor a 6 (persona natural)
     * - El último dígito es el verificador (algoritmo módulo 10)
     */
    private void validarCedulaEcuatoriana(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            throw new ReglaNegocioException("La cédula debe tener 10 dígitos numéricos");
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            throw new ReglaNegocioException("La cédula " + cedula + " tiene un código de provincia inválido");
        }
        if (cedula.charAt(2) - '0' >= 6) {
            throw new ReglaNegocioException("La cédula " + cedula + " no corresponde a una persona natural");
        }

        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = cedula.charAt(i) - '0';
            int producto = digito * (i % 2 == 0 ? 2 : 1);
            suma += producto > 9 ? producto - 9 : producto;
        }
        int verificadorEsperado = (10 - (suma % 10)) % 10;
        int verificadorReal = cedula.charAt(9) - '0';

        if (verificadorEsperado != verificadorReal) {
            throw new ReglaNegocioException("La cédula " + cedula + " no es válida (dígito verificador incorrecto)");
        }
    }

    private ClienteDTO aDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setCedula(cliente.getCedula());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setFechaVencimientoMembresia(cliente.getFechaVencimientoMembresia());
        dto.setPlanId(cliente.getPlan() != null ? cliente.getPlan().getId() : null);
        dto.setEntrenadorId(cliente.getEntrenador() != null ? cliente.getEntrenador().getId() : null);
        return dto;
    }
}
