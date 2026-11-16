package pe.com.nutrisalud.api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;
import pe.com.nutrisalud.api.service.IUsuarioService;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

	private final IUsuarioRepository usuarioRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void cambiarMiPassword(String correo, String passwordActual, String passwordNueva) {
	
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new RuntimeException("La contraseña actual es incorrecta"));
		
		if(!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
			throw new RuntimeException("La contraseña actual es incorrecta");
		}
		
		usuario.setPassword(passwordEncoder.encode(passwordNueva));
		usuarioRepo.save(usuario);
		
	}
	

	@Override
	public void resetearPasswordPorAdmin(Long usuarioId, String nuevaPassword) {
		
		Usuario usuario = usuarioRepo.findById(usuarioId)
				.orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
	
		usuario.setPassword(passwordEncoder.encode(nuevaPassword));
		usuarioRepo.save(usuario);
	}

}
