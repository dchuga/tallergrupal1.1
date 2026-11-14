package ec.edu.espe.gimnasio_api.repository;

import ec.edu.espe.gimnasio_api.entity.Asistencia;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByClienteIdOrderByFechaHoraIngresoDesc(Long clienteId);

    // Consulta personalizada 3 (JPQL): asistencias de un cliente en un rango
    @Query("SELECT a FROM Asistencia a WHERE a.cliente.id = :clienteId AND a.fechaHoraIngreso BETWEEN :desde AND :hasta")
    List<Asistencia> buscarEnRango(@Param("clienteId") Long clienteId,
                                   @Param("desde") LocalDateTime desde,
                                   @Param("hasta") LocalDateTime hasta);
}
