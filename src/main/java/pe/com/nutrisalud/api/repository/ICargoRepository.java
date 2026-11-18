package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.nutrisalud.api.model.entity.Cargo;

import java.util.Optional;

public interface ICargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNombre(String nombre);
}
