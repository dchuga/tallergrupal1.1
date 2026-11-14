package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.dto.PlanDTO;
import ec.edu.espe.gimnasio_api.entity.Plan;
import ec.edu.espe.gimnasio_api.exception.RecursoNoEncontradoException;
import ec.edu.espe.gimnasio_api.exception.ReglaNegocioException;
import ec.edu.espe.gimnasio_api.repository.ClienteRepository;
import ec.edu.espe.gimnasio_api.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final ClienteRepository clienteRepository;

    public PlanServiceImpl(PlanRepository planRepository, ClienteRepository clienteRepository) {
        this.planRepository = planRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanDTO> listarTodos() {
        return planRepository.findAll().stream()
                .map(this::aDTO)
                .toList();
    }

    @Override
    public PlanDTO registrar(PlanDTO dto) {
        String nombre = dto.getNombre().trim();

        // Regla: no pueden existir dos planes con el mismo nombre
        if (planRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe un plan con el nombre '" + nombre + "'");
        }
        validarPrecio(dto.getPrecio());

        Plan plan = new Plan();
        plan.setNombre(nombre);
        plan.setPrecio(dto.getPrecio());
        plan.setDuracionDias(dto.getDuracionDias());

        return aDTO(planRepository.save(plan));
    }

    @Override
    public PlanDTO actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        Plan plan = buscarPlan(id);
        validarPrecio(nuevoPrecio);

        plan.setPrecio(nuevoPrecio);
        return aDTO(planRepository.save(plan));
    }

    @Override
    public void eliminar(Long id) {
        Plan plan = buscarPlan(id);

        // Regla: no se puede eliminar un plan que tiene clientes inscritos
        if (clienteRepository.existsByPlanId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el plan '" + plan.getNombre() + "' porque tiene clientes asociados");
        }
        planRepository.delete(plan);
    }

    // ---------- Métodos de apoyo ----------

    private Plan buscarPlan(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un plan con id " + id));
    }

    // El PATCH de precio llega como @RequestParam (sin @Valid), por eso se valida aquí también
    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El precio del plan debe ser mayor a 0");
        }
        if (precio.scale() > 2) {
            throw new ReglaNegocioException("El precio solo admite hasta 2 decimales");
        }
    }

    private PlanDTO aDTO(Plan plan) {
        PlanDTO dto = new PlanDTO();
        dto.setId(plan.getId());
        dto.setNombre(plan.getNombre());
        dto.setPrecio(plan.getPrecio());
        dto.setDuracionDias(plan.getDuracionDias());
        return dto;
    }
}
