package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.List;

public interface IEvaluacionIMCRepository extends JpaRepository<EvaluacionIMC, Long> {

    // Metodo para ver el historial del IMC del trabajador
    List<EvaluacionIMC> findByTrabajadorIdOrderByFechaEvaluacionDesc(Long trabajadorId);

    // Filtro para el Dashboard del Médico
    @Query("SELECT e FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId AND (:estado IS NULL OR e.estado = :estado) ORDER BY e.fechaEvaluacion DESC")
    List<EvaluacionIMC> filtrarPorEmpresaYEstado(@Param("empresaId") Long empresaId, @Param("estado") EstadoEvaluacion estado);
}
