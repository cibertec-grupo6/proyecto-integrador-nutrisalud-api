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
public class EstadisticasSaltoEmpresaDTO {
    private Long empresaId;
    private String nombreEmpresa;
    private Integer totalEvaluaciones;
    private Double alturaPromedioCm;
    private Double alturaMinimaCm;
    private Double alturaMaximaCm;
    // Mapa para el gráfico de torta: {"DEFICIENTE": 2, "REGULAR": 8, "BUENO": 15, ...}
    private Map<String, Integer> distribucionRendimiento;
}