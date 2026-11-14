package ec.edu.espe.gimnasio_api.controller;

import ec.edu.espe.gimnasio_api.dto.ClienteDTO;
import ec.edu.espe.gimnasio_api.dto.EntrenadorDTO;
import ec.edu.espe.gimnasio_api.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public ResponseEntity<List<EntrenadorDTO>> listarTodos() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EntrenadorDTO> registrar(@Valid @RequestBody EntrenadorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenadorService.registrar(dto));
    }

    @GetMapping("/{id}/clientes")
    public ResponseEntity<List<ClienteDTO>> listarClientesAsignados(@PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.listarClientesAsignados(id));
    }
}
