package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionSaltoResponse {
    private Long id;
    private Long trabajadorId;
    private String nombreTrabajador;
    private Double alcanceReposoCm;
    private Double alcanceSaltoCm;
    private Double alturaSaltoCm;
    private ClasificacionSalto clasificacionRendimiento;
    private LocalDate fechaEvaluacion;
    private EstadoEvaluacion estado;
    private String tipoEvaluacion; // Siempre será "SALTO_VERTICAL"
    private String nombreEvaluador;
    private String cargoEvaluador;
}