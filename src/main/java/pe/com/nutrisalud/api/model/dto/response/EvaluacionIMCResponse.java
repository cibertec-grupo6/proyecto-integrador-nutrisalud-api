package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.nutrisalud.api.model.enums.ClasificacionIMC;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluacionIMCResponse {
    private Long id;
    private Long trabajadorId;
    private String nombreTrabajador;
    private Double pesoKg;
    private Double tallaM;
    private Double imcCalculado;
    private ClasificacionIMC clasificacion;
    private LocalDate fechaEvaluacion;
    private EstadoEvaluacion estado;
    private String tipoEvaluacion;
    private String nombreEvaluador;
    private String cargoEvaluador;
}
