package pe.com.nutrisalud.api.service;

public interface IUsuarioService {
	
	void cambiarMiPassword(String correo, String passwordActual, String passwordNueva);
    void resetearPasswordPorAdmin(Long usuarioId, String nuevaPassword);
}
