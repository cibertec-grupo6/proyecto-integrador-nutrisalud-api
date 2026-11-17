package pe.com.nutrisalud.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EvaluacionIMCRequest {

    @NotNull(message = "El ID del trabajador es obligatorio")
    private Long trabajadorId;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a cero")
    private Double pesoKg;

    @NotNull(message = "La talla es obligatoria")
    @Positive(message = "La talla debe ser mayor a cero")
    private Double tallaM;
}
