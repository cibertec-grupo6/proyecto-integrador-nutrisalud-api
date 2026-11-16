package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaResponse {
    private Long id;
    private String razonSocial;
    private String ruc;
    private String sector;
    private String direccion;
    private String correoContacto;
    private String telefono;
    private LocalDateTime fechaRegistro;
}