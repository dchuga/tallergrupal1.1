package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.ClienteDTO;
import ec.edu.espe.gimnasio_api.dto.EntrenadorDTO;
import ec.edu.espe.gimnasio_api.entity.Cliente;
import ec.edu.espe.gimnasio_api.entity.Entrenador;
import ec.edu.espe.gimnasio_api.exception.RecursoNoEncontradoException;
import ec.edu.espe.gimnasio_api.exception.ReglaNegocioException;
import ec.edu.espe.gimnasio_api.repository.ClienteRepository;
import ec.edu.espe.gimnasio_api.repository.EntrenadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntrenadorServiceImpl implements EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final ClienteRepository clienteRepository;

    public EntrenadorServiceImpl(EntrenadorRepository entrenadorRepository, ClienteRepository clienteRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrenadorDTO> listarTodos() {
        return entrenadorRepository.findAll().stream()
                .map(this::aDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EntrenadorDTO obtenerPorId(Long id) {
        return aDTO(buscarEntrenador(id));
    }

    @Override
    public EntrenadorDTO registrar(EntrenadorDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();

        // Regla: no pueden existir dos entrenadores con el mismo email
        if (entrenadorRepository.existsByEmail(email)) {
            throw new ReglaNegocioException("Ya existe un entrenador registrado con el email " + email);
        }

        Entrenador entrenador = new Entrenador();
        entrenador.setNombre(dto.getNombre().trim());
        entrenador.setApellido(dto.getApellido().trim());
        entrenador.setEspecialidad(dto.getEspecialidad() != null ? dto.getEspecialidad().trim() : null);
        entrenador.setEmail(email);

        return aDTO(entrenadorRepository.save(entrenador));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientesAsignados(Long entrenadorId) {
        // Si el entrenador no existe se responde 404 (y no una lista vacía engañosa)
        buscarEntrenador(entrenadorId);
        // Usa la consulta personalizada JPQL buscarPorEntrenador del repositorio
        return clienteRepository.buscarPorEntrenador(entrenadorId).stream()
                .map(this::clienteADTO)
                .toList();
    }

    // ---------- Métodos de apoyo ----------

    private Entrenador buscarEntrenador(Long id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un entrenador con id " + id));
    }

    private EntrenadorDTO aDTO(Entrenador entrenador) {
        EntrenadorDTO dto = new EntrenadorDTO();
        dto.setId(entrenador.getId());
        dto.setNombre(entrenador.getNombre());
        dto.setApellido(entrenador.getApellido());
        dto.setEspecialidad(entrenador.getEspecialidad());
        dto.setEmail(entrenador.getEmail());
        return dto;
    }

    private ClienteDTO clienteADTO(Cliente cliente) {
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
