package ec.edu.espe.gimnasio_api.repository;

import ec.edu.espe.gimnasio_api.entity.Cliente;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCedula(String cedula);

    // Consulta personalizada 1 (Derived Query): membresias vencidas
    List<Cliente> findByFechaVencimientoMembresiaBefore(LocalDate fecha);

    // Consulta personalizada 2 (JPQL): clientes de un entrenador
    @Query("SELECT c FROM Cliente c WHERE c.entrenador.id = :entrenadorId ORDER BY c.nombre")
    List<Cliente> buscarPorEntrenador(@Param("entrenadorId") Long entrenadorId);

    // Consultas derivadas usadas por ClienteServiceImpl y PlanServiceImpl (reglas de negocio)
    boolean existsByEmail(String email);

    boolean existsByCedulaAndIdNot(String cedula, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPlanId(Long planId);
}
