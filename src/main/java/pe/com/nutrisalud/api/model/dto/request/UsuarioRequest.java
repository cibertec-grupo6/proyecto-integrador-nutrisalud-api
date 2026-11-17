package pe.com.nutrisalud.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.com.nutrisalud.api.model.enums.Rol;

@Data
public class UsuarioRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los Apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El correo es obligarorio")
    @Email(message = "El formato del correo es no es válido")
    private String correo;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    // Opcional: Si no se envia el sistema asignara uan por defecto
    private String password;
}
