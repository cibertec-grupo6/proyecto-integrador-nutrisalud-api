package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialTrabajadorDTO {
    private Long trabajadorId;
    private String nombreTrabajador;
    private String documentoIdentidad;
    private String nombreEmpresa;
    private String cargoTrabajador;


    private List<EvaluacionIMCResponse> historialIMC;
    private List<EvaluacionSaltoResponse> historialSalto;
}