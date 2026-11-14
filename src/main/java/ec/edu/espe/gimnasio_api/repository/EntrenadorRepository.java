package ec.edu.espe.gimnasio_api.repository;

import ec.edu.espe.gimnasio_api.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    boolean existsByEmail(String email);
}
