package pe.com.nutrisalud.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.com.nutrisalud.api.model.dto.request.LoginRequest;
import pe.com.nutrisalud.api.model.dto.response.LoginResponse;
import pe.com.nutrisalud.api.security.JwtTokenProvider;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserDetailsService userDetailsService;
	
	
	 @PostMapping("/login")
	    public ResponseEntity<?> autenticarUsuario(@RequestBody LoginRequest loginRequest) {
	        try {
	            Authentication authentication = authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getPassword())
	            );
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            String jwt = tokenProvider.generateToken(loginRequest.getCorreo());

	            // Cargar detalles del usuario para obtener el rol
	            var userDetails = userDetailsService.loadUserByUsername(loginRequest.getCorreo());
	            String rol = userDetails.getAuthorities().iterator().next().getAuthority();

	            return ResponseEntity.ok(new LoginResponse(jwt, "Bearer", loginRequest.getCorreo(), rol));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Credenciales inválidas: " + e.getMessage());
	        }
	    }
	}