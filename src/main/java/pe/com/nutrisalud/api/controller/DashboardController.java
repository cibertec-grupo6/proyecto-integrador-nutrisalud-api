package pe.com.nutrisalud.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.response.*;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.service.IDashboardService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('MEDICO')")
public class DashboardController {

    private final IDashboardService dashboardService;


    @GetMapping("/trabajadores/{trabajadorId}/historial")
    public ResponseEntity<HistorialTrabajadorDTO> obtenerHistorial(@PathVariable Long trabajadorId) {
        return ResponseEntity.ok(dashboardService.obtenerHistorialTrabajador(trabajadorId));
    }


    @GetMapping("/evaluaciones")
    public ResponseEntity<Map<String, Object>> obtenerEvaluacionesFiltradas(
            @RequestParam Long empresaId,
            @RequestParam(required = false) EstadoEvaluacion estado) {

        return ResponseEntity.ok(dashboardService.obtenerEvaluacionesFiltradas(empresaId, estado));
    }

    //Resumen general (las tarjetas de arriba del dashboard)
    @GetMapping("/resumen")
    public ResponseEntity<ResumenDashboardDTO> obtenerResumenGeneral() {
        return ResponseEntity.ok(dashboardService.obtenerResumenGeneral());
    }

    //Estadísticas de IMC por empresa (para gráfico de torta)
    @GetMapping("/estadisticas/imc")
    public ResponseEntity<EstadisticasIMCEmpresaDTO> obtenerEstadisticasIMC(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticasIMCPorEmpresa(empresaId));
    }

    //Estadísticas de Salto Vertical por empresa (para gráfico de torta)
    @GetMapping("/estadisticas/salto")
    public ResponseEntity<EstadisticasSaltoEmpresaDTO> obtenerEstadisticasSalto(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticasSaltoPorEmpresa(empresaId));
    }

    //Evolución individual del trabajador (para gráficos de líneas)
    @GetMapping("/trabajadores/{trabajadorId}/evolucion")
    public ResponseEntity<EvolucionTrabajadorDTO> obtenerEvolucionTrabajador(@PathVariable Long trabajadorId) {
        return ResponseEntity.ok(dashboardService.obtenerEvolucionTrabajador(trabajadorId));
    }
}