package pe.com.nutrisalud.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EvaluacionSaltoRequest {

    @NotNull(message = "El ID del trabajador es obligatorio")
    private Long trabajadorId;

    @NotNull(message = "El alcance en reposo es obligatorio")
    @Positive(message = "El alcance en reposo debe ser mayor a cero")
    private Double alcanceReposoCm;

    @NotNull(message = "El alcance con salto es obligatorio")
    @Positive(message = "El alcance con salto debe ser mayor a cero")
    private Double alcanceSaltoCm;
}