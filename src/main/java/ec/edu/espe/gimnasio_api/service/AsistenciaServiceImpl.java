package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.AsistenciaDTO;
import ec.edu.espe.gimnasio_api.entity.Asistencia;
import ec.edu.espe.gimnasio_api.entity.Cliente;
import ec.edu.espe.gimnasio_api.exception.MembresiaVencidaException;
import ec.edu.espe.gimnasio_api.exception.RecursoNoEncontradoException;
import ec.edu.espe.gimnasio_api.exception.ReglaNegocioException;
import ec.edu.espe.gimnasio_api.repository.AsistenciaRepository;
import ec.edu.espe.gimnasio_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteRepository clienteRepository;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository, ClienteRepository clienteRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaDTO> listarPorCliente(Long clienteId) {
        // Si el cliente no existe se responde 404 (y no una lista vacía engañosa)
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("No existe un cliente con id " + clienteId);
        }
        return asistenciaRepository.findByClienteIdOrderByFechaHoraIngresoDesc(clienteId).stream()
                .map(this::aDTO)
                .toList();
    }

    @Override
    public AsistenciaDTO registrarAsistencia(AsistenciaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + dto.getClienteId()));

        LocalDateTime ingreso = dto.getFechaHoraIngreso() != null ? dto.getFechaHoraIngreso() : LocalDateTime.now();
        if (ingreso.isAfter(LocalDateTime.now())) {
            throw new ReglaNegocioException("No se puede registrar una asistencia con fecha futura");
        }

        // Regla principal: validación automática de la vigencia de la membresía
        LocalDate vencimiento = cliente.getFechaVencimientoMembresia();
        if (vencimiento == null || ingreso.toLocalDate().isAfter(vencimiento)) {
            throw new MembresiaVencidaException(
                    "La membresía del cliente " + cliente.getNombre() + " está vencida desde " + vencimiento
                            + ". Debe renovar su plan para ingresar");
        }

        // Regla: solo un ingreso por cliente por día
        LocalDateTime inicioDia = ingreso.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1).minusNanos(1);
        if (!asistenciaRepository.buscarEnRango(cliente.getId(), inicioDia, finDia).isEmpty()) {
            throw new ReglaNegocioException(
                    "El cliente " + cliente.getNombre() + " ya registró su asistencia el " + ingreso.toLocalDate());
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setCliente(cliente);
        asistencia.setFechaHoraIngreso(ingreso);

        return aDTO(asistenciaRepository.save(asistencia));
    }

    // ---------- Métodos de apoyo ----------

    private AsistenciaDTO aDTO(Asistencia asistencia) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(asistencia.getId());
        dto.setClienteId(asistencia.getCliente().getId());
        dto.setFechaHoraIngreso(asistencia.getFechaHoraIngreso());
        return dto;
    }
}
