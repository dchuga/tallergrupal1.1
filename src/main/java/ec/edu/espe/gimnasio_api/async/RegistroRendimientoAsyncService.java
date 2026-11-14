package ec.edu.espe.gimnasio_api.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RegistroRendimientoAsyncService {

    private static final Logger log =
            LoggerFactory.getLogger(RegistroRendimientoAsyncService.class);

    @Async
    public void registrar(String metodo, double duracionMs) {
        log.info("Registro asincrono | Metodo: {} | Tiempo: {} ms",
                metodo, duracionMs);
    }
}
