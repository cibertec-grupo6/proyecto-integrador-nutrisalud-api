package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.response.*;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.Map;

public interface IDashboardService {


    HistorialTrabajadorDTO obtenerHistorialTrabajador(Long trabajadorId);

    // HU10: Filtros para el dashboard (Retorna un mapa con listas de IMC y Salto filtradas)
    Map<String, Object> obtenerEvaluacionesFiltradas(Long empresaId, EstadoEvaluacion estado);


    // NUEVOS MÉTODOS ESTADÍSTICOS
    ResumenDashboardDTO obtenerResumenGeneral();
    EstadisticasIMCEmpresaDTO obtenerEstadisticasIMCPorEmpresa(Long empresaId);
    EstadisticasSaltoEmpresaDTO obtenerEstadisticasSaltoPorEmpresa(Long empresaId);

    EvolucionTrabajadorDTO obtenerEvolucionTrabajador(Long trabajadorId);
}
