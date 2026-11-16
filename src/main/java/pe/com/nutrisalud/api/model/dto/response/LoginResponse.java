package pe.com.nutrisalud.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

	private String token;
	
	private String tipo;
	
	private String correo; //" Bearer"
	
	private String rol;
}
