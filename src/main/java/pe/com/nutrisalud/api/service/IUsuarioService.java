package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.UsuarioRequest;
import pe.com.nutrisalud.api.model.dto.response.PerfilUsuarioResponse;
import pe.com.nutrisalud.api.model.dto.response.UsuarioResponse;

import java.util.List;

public interface IUsuarioService {
	
	void cambiarMiPassword(String correo, String passwordActual, String passwordNueva);
    void resetearPasswordPorAdmin(Long usuarioId, String nuevaPassword);

    UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest);
    List<UsuarioResponse> listarUsuarios();
    UsuarioResponse cambiarEstadoUsuario(Long id, Boolean activo);
    PerfilUsuarioResponse obtenerPerfilActual(String correo);
}
