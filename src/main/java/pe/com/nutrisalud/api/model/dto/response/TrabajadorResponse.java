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
public class TrabajadorResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String documentoIdentidad;
    private String correo;
    private String cargo;
    private String area;
    private Long empresaId;
    private String razonSocialEmpresa; // Para mostrar el nombre de la empresa en la respuesta
    private LocalDateTime fechaRegistro;
}
