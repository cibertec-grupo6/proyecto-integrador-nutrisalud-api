package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.nutrisalud.api.model.entity.Empresa;

import java.util.Optional;

public interface IEmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByRuc(String ruc);
    Optional<Empresa> findByRuc(String ruc);
}
