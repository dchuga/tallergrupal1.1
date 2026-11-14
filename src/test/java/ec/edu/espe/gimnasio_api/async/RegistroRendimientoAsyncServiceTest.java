package ec.edu.espe.gimnasio_api.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistroRendimientoAsyncServiceTest {

    @Autowired
    private RegistroRendimientoAsyncService servicio;

    @Test
    void servicioTieneProxyAsincrono() {
        assertTrue(AopUtils.isAopProxy(servicio));
    }

    @Test
    void metodoTieneAnotacionAsync() throws Exception {
        var metodo = RegistroRendimientoAsyncService.class
                .getMethod("registrar", String.class, double.class);

        assertNotNull(metodo.getAnnotation(Async.class));
    }
}
