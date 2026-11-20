package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenDashboardDTO {
    // Tarjetas de resumen (las que van arriba del dashboard)
    private Long totalTrabajadores;
    private Long totalEmpresas;
    private Long evaluacionesPendientes;
    private Long evaluacionesAprobadas;
    private Long evaluacionesEnviadas;
    private Long evaluacionesHoy;
}