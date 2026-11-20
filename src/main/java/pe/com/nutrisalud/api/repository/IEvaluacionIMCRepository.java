package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.enums.ClasificacionIMC;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.List;

public interface IEvaluacionIMCRepository extends JpaRepository<EvaluacionIMC, Long> {

    // Metodo para ver el historial del IMC del trabajador
    List<EvaluacionIMC> findByTrabajadorIdOrderByFechaEvaluacionDesc(Long trabajadorId);

    // Filtro para el Dashboard del Médico
    @Query("SELECT e FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId AND (:estado IS NULL OR e.estado = :estado) ORDER BY e.fechaEvaluacion DESC")
    List<EvaluacionIMC> filtrarPorEmpresaYEstado(@Param("empresaId") Long empresaId, @Param("estado") EstadoEvaluacion estado);

    @Query("SELECT AVG(e.imcCalculado) FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularIMCPromedioPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT MIN(e.imcCalculado) FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularIMCMinimoPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT MAX(e.imcCalculado) FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId")
    Double calcularIMCMaximoPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT COUNT(e) FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId")
    Long contarEvaluacionesPorEmpresa(@Param("empresaId") Long empresaId);

    @Query("SELECT COUNT(e) FROM EvaluacionIMC e WHERE e.trabajador.empresa.id = :empresaId AND e.clasificacion = :clasificacion")
    Long contarPorClasificacion(@Param("empresaId") Long empresaId, @Param("clasificacion") ClasificacionIMC clasificacion);


    @Query("SELECT COUNT(e) FROM EvaluacionIMC e WHERE e.estado = :estado")
    Long contarPorEstado(@Param("estado") EstadoEvaluacion estado);

    @Query("SELECT COUNT(e) FROM EvaluacionIMC e WHERE FUNCTION('DATE', e.fechaRegistro) = CURRENT_DATE")
    Long contarEvaluacionesHoy();

    // Para el último IMC del trabajador
    @Query("SELECT e FROM EvaluacionIMC e WHERE e.trabajador.id = :trabajadorId ORDER BY e.fechaEvaluacion DESC LIMIT 1")
    EvaluacionIMC encontrarUltimaEvaluacionIMC(@Param("trabajadorId") Long trabajadorId);

    @Query("SELECT AVG(e.imcCalculado) FROM EvaluacionIMC e WHERE e.trabajador.id = :trabajadorId")
    Double calcularIMCPromedioTrabajador(@Param("trabajadorId") Long trabajadorId);

    @Query("SELECT COUNT(e) FROM EvaluacionIMC e WHERE e.trabajador.id = :trabajadorId")
    Long contarEvaluacionesTrabajador(@Param("trabajadorId") Long trabajadorId);
}
