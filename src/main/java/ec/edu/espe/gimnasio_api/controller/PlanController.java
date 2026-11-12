package ec.edu.espe.gimnasio_api.controller;

import ec.edu.espe.gimnasio_api.dto.PlanDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/planes")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public ResponseEntity<List<PlanDTO>> listarTodos() {
        return ResponseEntity.ok(planService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<PlanDTO> registrar(@Valid @RequestBody PlanDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.registrar(dto));
    }

    @PatchMapping("/{id}/precio")
    public ResponseEntity<PlanDTO> actualizarPrecio(@PathVariable Long id, @RequestParam BigDecimal precio) {
        return ResponseEntity.ok(planService.actualizarPrecio(id, precio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}