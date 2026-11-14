package ec.edu.espe.gimnasio_api.aspect;

import ec.edu.espe.gimnasio_api.async.RegistroRendimientoAsyncService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RendimientoAspect {

    private final RegistroRendimientoAsyncService registroAsync;

    public RendimientoAspect(
            RegistroRendimientoAsyncService registroAsync) {
        this.registroAsync = registroAsync;
    }

    @Around("execution(public * ec.edu.espe.gimnasio_api.service..*(..))")
    public Object medirRendimiento(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long inicio = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {
            double duracionMs =
                    (System.nanoTime() - inicio) / 1_000_000.0;

            registroAsync.registrar(
                    joinPoint.getSignature().toShortString(),
                    duracionMs
            );
        }
    }
}
