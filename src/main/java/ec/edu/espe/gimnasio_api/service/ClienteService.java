package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.ClienteDTO;
import java.util.List;

public interface ClienteService {
    List<ClienteDTO> listarTodos();
    ClienteDTO obtenerPorId(Long id);
    List<ClienteDTO> listarMembresiasVencidas();
    ClienteDTO registrar(ClienteDTO dto);
    ClienteDTO actualizar(Long id, ClienteDTO dto);
    void eliminar(Long id);
}