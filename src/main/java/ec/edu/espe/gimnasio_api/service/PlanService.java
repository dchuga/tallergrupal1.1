package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.PlanDTO;
import java.math.BigDecimal;
import java.util.List;

public interface PlanService {
    List<PlanDTO> listarTodos();
    PlanDTO registrar(PlanDTO dto);
    PlanDTO actualizarPrecio(Long id, BigDecimal nuevoPrecio);
    void eliminar(Long id);
}