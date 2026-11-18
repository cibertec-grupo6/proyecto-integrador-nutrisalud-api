package pe.com.nutrisalud.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pe.com.nutrisalud.api.model.entity.Cargo;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.model.enums.Rol;
import pe.com.nutrisalud.api.repository.ICargoRepository;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final IUsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final ICargoRepository cargoRepository;

	@Override
	public void run(String... args) {

		String adminCorreo = "admin1@nutrisalud.com";

		// 1. Buscar el cargo, o crearlo si no existe en la BD
		Cargo cargoAdmin = cargoRepository.findByNombre("Administrador de Sistemas")
				.orElseGet(() -> cargoRepository.save(Cargo.builder()
						.nombre("Administrador de Sistemas")
						.descripcion("Gestión de usuarios y configuración del sistema")
						.build()));

		if (usuarioRepository.findByCorreo(adminCorreo).isEmpty()) {
			Usuario admin = Usuario.builder()
					.nombres("Administrador")
					.apellidos("Sistema")
					.correo(adminCorreo)
					.password(passwordEncoder.encode("admin123"))// Contraseña por defecto
					.rol(Rol.ADMIN).activo(true)
					.cargo(cargoAdmin)
					.build();

			usuarioRepository.save(admin);
			System.out.println("Usuario ADMIN creado exitosamente:" + adminCorreo + " / admin123");

		}

	}

}
