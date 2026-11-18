package pe.com.nutrisalud.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.response.HistorialTrabajadorDTO;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.service.IDashboardService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('MEDICO')") // 🔒 Todo este controller es exclusivo para el Médico
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
}