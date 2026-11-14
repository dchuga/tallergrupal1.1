package ec.edu.espe.gimnasio_api.controller;

import ec.edu.espe.gimnasio_api.dto.PlanDTO;
import ec.edu.espe.gimnasio_api.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanControllerTest {

    private final PlanService servicio = mock(PlanService.class);
    private final PlanController controlador = new PlanController(servicio);

    @Test
    void listarPlanesDevuelveOk() {
        when(servicio.listarTodos()).thenReturn(List.of(new PlanDTO()));

        var respuesta = controlador.listarTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(servicio).listarTodos();
    }

    @Test
    void registrarPlanDevuelveCreated() {
        PlanDTO plan = new PlanDTO();
        when(servicio.registrar(plan)).thenReturn(plan);

        var respuesta = controlador.registrar(plan);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(plan, respuesta.getBody());
    }

    @Test
    void eliminarPlanDevuelveNoContent() {
        var respuesta = controlador.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(servicio).eliminar(1L);
    }
}
