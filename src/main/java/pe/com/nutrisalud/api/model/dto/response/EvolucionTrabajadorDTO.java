package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvolucionTrabajadorDTO {
    private Long trabajadorId;
    private String nombreTrabajador;
    private String documentoIdentidad;
    private String nombreEmpresa;


    private Integer totalEvaluacionesIMC;
    private Double imcPromedio;
    private Double imcActual; // El más reciente
    private String clasificacionActual;

    private Integer totalEvaluacionesSalto;
    private Double alturaSaltoPromedioCm;
    private Double alturaSaltoActualCm;
    private String clasificacionSaltoActual;


    private List<PuntoEvolucion> evolucionIMC;

    private List<PuntoEvolucion> evolucionSalto;

    private List<String> alertas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PuntoEvolucion {
        private LocalDate fecha;
        private Double valor;
        private String clasificacion;
    }
}