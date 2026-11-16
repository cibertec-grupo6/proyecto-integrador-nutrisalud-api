package pe.com.nutrisalud.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambiarPasswordRequest {

	@NotBlank(message = "La contraseña actual es obligatoria")
	private String passwordActual;
	
	@NotBlank(message = "La nueva contraseña es obligatoria")
	private String passwordNueva;
}
