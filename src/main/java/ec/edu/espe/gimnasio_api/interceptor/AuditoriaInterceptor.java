package ec.edu.espe.gimnasio_api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long inicio = System.currentTimeMillis();
        request.setAttribute("tiempoInicio", inicio);

        String origen = request.getHeader("X-Client-App");
        if (origen == null || origen.isBlank()) {
            origen = "Cliente-Web-Desconocido";
        }

        log.info("[GIMNASIO-IN] {} {} | IP: {} | Origen: {}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), origen);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long inicio = (Long) request.getAttribute("tiempoInicio");
        long duracion = (inicio != null) ? (System.currentTimeMillis() - inicio) : 0;

        log.info("[GIMNASIO-OUT] {} | Status: {} | Tiempo: {} ms",
                request.getRequestURI(), response.getStatus(), duracion);
    }
}