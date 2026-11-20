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
public class PerfilUsuarioResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String correo;
    private Rol rol;
    private String nombreCargo;      // ← El cargo del usuario
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}