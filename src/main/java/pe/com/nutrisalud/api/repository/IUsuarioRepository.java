package pe.com.nutrisalud.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.nutrisalud.api.model.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	
	// Metodo para buscar un usuario por su correo(usando el login)
	Optional<Usuario> findByCorreo(String correo);
	
	// Metodo personalizado para verificar si un correo ya existe
	boolean existsByCorreo(String correo);


}
