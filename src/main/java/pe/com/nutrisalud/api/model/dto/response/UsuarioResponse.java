package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.nutrisalud.api.model.enums.Rol;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String correo;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private String nombreCargo;
}
