package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.ClienteDTO;
import ec.edu.espe.gimnasio_api.dto.EntrenadorDTO;
import java.util.List;

public interface EntrenadorService {
    List<EntrenadorDTO> listarTodos();
    EntrenadorDTO obtenerPorId(Long id);
    EntrenadorDTO registrar(EntrenadorDTO dto);
    List<ClienteDTO> listarClientesAsignados(Long entrenadorId);
}
