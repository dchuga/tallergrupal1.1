package ec.edu.espe.gimnasio_api.controller;

import ec.edu.espe.gimnasio_api.dto.PlanDTO;
import ec.edu.espe.gimnasio_api.dto.AsistenciaDTO;
import ec.edu.espe.gimnasio_api.service.AsistenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AsistenciaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(asistenciaService.listarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<AsistenciaDTO> registrarAsistencia(@Valid @RequestBody AsistenciaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.registrarAsistencia(dto));
    }
}