package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.response.HistorialTrabajadorDTO;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.util.Map;

public interface IDashboardService {


    HistorialTrabajadorDTO obtenerHistorialTrabajador(Long trabajadorId);

    // HU10: Filtros para el dashboard (Retorna un mapa con listas de IMC y Salto filtradas)
    Map<String, Object> obtenerEvaluacionesFiltradas(Long empresaId, EstadoEvaluacion estado);
}
