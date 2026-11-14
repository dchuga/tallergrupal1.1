package ec.edu.espe.gimnasio_api.repository;

import ec.edu.espe.gimnasio_api.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    boolean existsByNombre(String nombre);
}
