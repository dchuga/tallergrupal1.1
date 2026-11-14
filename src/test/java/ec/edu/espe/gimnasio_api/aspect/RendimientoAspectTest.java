package ec.edu.espe.gimnasio_api.aspect;

import ec.edu.espe.gimnasio_api.async.RegistroRendimientoAsyncService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RendimientoAspectTest {

    @Test
    void registraTiempoDeEjecucion() throws Throwable {

        RegistroRendimientoAsyncService servicio =
                mock(RegistroRendimientoAsyncService.class);

        ProceedingJoinPoint joinPoint =
                mock(ProceedingJoinPoint.class);

        Signature firma = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(firma);
        when(firma.toShortString()).thenReturn("ClienteService.listarTodos()");
        when(joinPoint.proceed()).thenReturn("correcto");

        RendimientoAspect aspecto = new RendimientoAspect(servicio);

        Object resultado = aspecto.medirRendimiento(joinPoint);

        assertEquals("correcto", resultado);

        verify(servicio).registrar(
                eq("ClienteService.listarTodos()"),
                anyDouble()
        );
    }

    @Test
    void registraTiempoInclusoSiOcurreError() throws Throwable {

        RegistroRendimientoAsyncService servicio =
                mock(RegistroRendimientoAsyncService.class);

        ProceedingJoinPoint joinPoint =
                mock(ProceedingJoinPoint.class);

        Signature firma = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(firma);
        when(firma.toShortString()).thenReturn("Servicio.metodo()");
        when(joinPoint.proceed())
                .thenThrow(new IllegalStateException("Error de prueba"));

        RendimientoAspect aspecto = new RendimientoAspect(servicio);

        assertThrows(IllegalStateException.class,
                () -> aspecto.medirRendimiento(joinPoint));

        verify(servicio).registrar(
                eq("Servicio.metodo()"),
                anyDouble()
        );
    }
}
