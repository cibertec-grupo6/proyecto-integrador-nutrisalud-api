package pe.com.nutrisalud.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TrabajadorRequest {
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    private String tipoDocumento;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "El documento debe contener solo números (8 a 15 dígitos)")
    private String documentoIdentidad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    private String cargo;
    private String area;

    @NotNull(message = "La empresa es obligatoria")
    private Long empresaId;
}
