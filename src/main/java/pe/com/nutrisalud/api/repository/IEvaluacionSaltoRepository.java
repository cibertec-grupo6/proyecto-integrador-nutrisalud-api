package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;
import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.List;

public interface IEvaluacionSaltoRepository extends JpaRepository<EvaluacionSalto, Long> {
    List<EvaluacionSalto> findByTrabajadorIdOrderByFechaEvaluacionDesc(Long trabajadorId);

    // Filtro para el Dashboard del Médico
    @Query("SELECT e FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId AND (:estado IS NULL OR e.estado = :estado) ORDER BY e.fechaEvaluacion DESC")
    List<EvaluacionSalto> filtrarPorEmpresaYEstado(@Param("empresaId") Long empresaId, @Param("estado") EstadoEvaluacion estado);



    //Consultas Estadísticas
    @Query("SELECT AVG(e.alturaSaltoCm) FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularAlturaPromedioPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT MIN(e.alturaSaltoCm) FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularAlturaMinimaPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT MAX(e.alturaSaltoCm) FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularAlturaMaximaPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT COUNT(e) FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId")
    Long contarEvaluacionesPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT COUNT(e) FROM EvaluacionSalto e WHERE e.trabajador.empresa.id = :empresaId AND e.clasificacionRendimiento = :clasificacion")
    Long contarPorClasificacion(@Param("empresaId") Long empresaId, @Param("clasificacion") ClasificacionSalto clasificacion);

    @Query("SELECT COUNT(e) FROM EvaluacionSalto e WHERE e.estado = :estado")
    Long contarPorEstado(@Param("estado") EstadoEvaluacion estado);

    @Query("SELECT COUNT(e) FROM EvaluacionSalto e WHERE FUNCTION('DATE', e.fechaRegistro) = CURRENT_DATE")
    Long contarEvaluacionesHoy();


    @Query("SELECT e FROM EvaluacionSalto e WHERE e.trabajador.id = :trabajadorId ORDER BY e.fechaEvaluacion DESC LIMIT 1")
    EvaluacionSalto encontrarUltimaEvaluacionSalto(@Param("trabajadorId") Long trabajadorId);

    @Query("SELECT AVG(e.alturaSaltoCm) FROM EvaluacionSalto e WHERE e.trabajador.id = :trabajadorId")
    Double calcularAlturaPromedioTrabajador(@Param("trabajadorId") Long trabajadorId);

    @Query("SELECT COUNT(e) FROM EvaluacionSalto e WHERE e.trabajador.id = :trabajadorId")
    Long contarEvaluacionesTrabajador(@Param("trabajadorId") Long trabajadorId);
}
