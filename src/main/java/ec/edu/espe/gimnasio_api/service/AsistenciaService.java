package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.AsistenciaDTO;
import java.util.List;

public interface AsistenciaService {
    List<AsistenciaDTO> listarPorCliente(Long clienteId);
    AsistenciaDTO registrarAsistencia(AsistenciaDTO dto);
}