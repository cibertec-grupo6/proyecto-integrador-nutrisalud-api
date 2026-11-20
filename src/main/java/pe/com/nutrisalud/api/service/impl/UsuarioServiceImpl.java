package pe.com.nutrisalud.api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.request.UsuarioRequest;
import pe.com.nutrisalud.api.model.dto.response.PerfilUsuarioResponse;
import pe.com.nutrisalud.api.model.dto.response.UsuarioResponse;
import pe.com.nutrisalud.api.model.entity.Cargo;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.repository.ICargoRepository;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;
import pe.com.nutrisalud.api.service.IUsuarioService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

	private final IUsuarioRepository usuarioRepo;
	
	private final PasswordEncoder passwordEncoder;

	private final ICargoRepository cargoRepo;
	
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

	@Override
	public UsuarioResponse crearUsuario(UsuarioRequest request) {
		// 1. Validar que el correo no exista
		if (usuarioRepo.existsByCorreo(request.getCorreo())) {
			throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + request.getCorreo());
		}

		// 2. Si el admin no envía password, asignar una por defecto
		String passwordFinal = (request.getPassword() != null && !request.getPassword().isEmpty())
				? request.getPassword()
				: "NutriSalud2026*"; // Contraseña por defecto segura

		Cargo cargo = cargoRepo.findById(request.getCargoId())
				.orElseThrow(() -> new ResourceNotFoundException("Cargo no encontrado con ID: " + request.getCargoId()));

		// 3. Crear y guardar
		Usuario nuevoUsuario = Usuario.builder()
				.nombres(request.getNombres())
				.apellidos(request.getApellidos())
				.correo(request.getCorreo())
				.password(passwordEncoder.encode(passwordFinal))
				.rol(request.getRol())
				.cargo(cargo)
				.activo(true) // Por defecto se crea activo
				.build();

		return mapToResponse(usuarioRepo.save(nuevoUsuario));
	}

	@Override
	public List<UsuarioResponse> listarUsuarios() {
		return usuarioRepo.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public UsuarioResponse cambiarEstadoUsuario(Long id, Boolean activo) {
		Usuario usuario = usuarioRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

		// No permitir que el admin se desactive a sí mismo por accidente
		if (usuario.getCorreo().equals("admin@nutrisalud.com") && !activo) {
			throw new IllegalArgumentException("No se puede desactivar el usuario administrador principal.");
		}

		usuario.setActivo(activo);
		return mapToResponse(usuarioRepo.save(usuario));
	}

	@Override
	public PerfilUsuarioResponse obtenerPerfilActual(String correo) {
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el correo: " + correo));

		return PerfilUsuarioResponse.builder()
				.id(usuario.getId())
				.nombres(usuario.getNombres())
				.apellidos(usuario.getApellidos())
				.correo(usuario.getCorreo())
				.rol(usuario.getRol())
				.nombreCargo(usuario.getCargo().getNombre()) // ← Aquí viene el cargo
				.activo(usuario.getActivo())
				.fechaCreacion(usuario.getFechaCreacion())
				.build();
	}

	// Método auxiliar para mapear (agrégalo si no lo tienes)
	private UsuarioResponse mapToResponse(Usuario usuario) {
		return UsuarioResponse.builder()
				.id(usuario.getId())
				.nombres(usuario.getNombres())
				.apellidos(usuario.getApellidos())
				.correo(usuario.getCorreo())
				.rol(usuario.getRol())
				.activo(usuario.getActivo())
				.nombreCargo(usuario.getCargo().getNombre())
				.fechaCreacion(usuario.getFechaCreacion())
				.build();
	}

}
