package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;

import java.util.List;

public interface IEvaluacionIMCRepository extends JpaRepository<EvaluacionIMC, Long> {

    // Metodo para ver el historial del IMC del trabajador
    //List<EvaluacionIMC> findByTrabajadorIdOrderByFechaEvaluacioneDesc(Long trabajadorId);
}
