package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.List;

public interface IEvaluacionSaltoRepository extends JpaRepository<EvaluacionSalto, Long> {
    List<EvaluacionSalto> findByTrabajadorIdOrderByFechaEvaluacionDesc(Long trabajadorId);

    // Filtro para el Dashboard del Médico
    @Query("SELECT e FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId AND (:estado IS NULL OR e.estado = :estado) ORDER BY e.fechaEvaluacion DESC")
    List<EvaluacionSalto> filtrarPorEmpresaYEstado(@Param("empresaId") Long empresaId, @Param("estado") EstadoEvaluacion estado);
}
