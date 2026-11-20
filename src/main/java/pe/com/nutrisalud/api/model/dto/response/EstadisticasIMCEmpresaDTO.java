package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasIMCEmpresaDTO {
    private Long empresaId;
    private String nombreEmpresa;
    private Integer totalEvaluaciones;
    private Double imcPromedio;
    private Double imcMinimo;
    private Double imcMaximo;

    private Map<String, Integer> distribucionClasificacion;
}