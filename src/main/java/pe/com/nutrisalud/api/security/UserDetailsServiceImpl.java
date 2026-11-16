package pe.com.nutrisalud.api.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
	
	
	private final IUsuarioRepository usuarioRepo;

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado" + correo));
		
		return new User(
				usuario.getCorreo(),
				usuario.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
				);
	}
	
}
