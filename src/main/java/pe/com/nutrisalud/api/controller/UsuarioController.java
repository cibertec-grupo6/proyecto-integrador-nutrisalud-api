package pe.com.nutrisalud.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pe.com.nutrisalud.api.model.dto.request.CambiarPasswordRequest;
import pe.com.nutrisalud.api.model.dto.request.ResetPasswordRequest;
import pe.com.nutrisalud.api.model.dto.request.UsuarioRequest;
import pe.com.nutrisalud.api.model.dto.response.PerfilUsuarioResponse;
import pe.com.nutrisalud.api.model.dto.response.UsuarioResponse;
import pe.com.nutrisalud.api.service.IUsuarioService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;


    // 1. Crear nuevo usuario (Enfermera o Médico)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse nuevoUsuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // 2. Listar todos los usuarios (Para ver cuántos médicos/enfermeras hay)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // 3. Activar o Desactivar usuario (Dar de baja)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        return ResponseEntity.ok(usuarioService.cambiarEstadoUsuario(id, activo));
    }


    // El usuario cambia su propia contraseña (requiere estar logueado)
    @PutMapping("/mi-password")
    public ResponseEntity<String> cambiarMiPassword(@Valid @RequestBody CambiarPasswordRequest request) {
        // Obtenemos el correo del usuario logueado desde el contexto de seguridad
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        
        usuarioService.cambiarMiPassword(correo, request.getPasswordActual(), request.getPasswordNueva());
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    // Solo el ADMIN puede resetear la contraseña de otro usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<String> resetearPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        usuarioService.resetearPasswordPorAdmin(id, request.getNuevaPassword());
        return ResponseEntity.ok("Contraseña del usuario reseteada exitosamente");
    }

    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PerfilUsuarioResponse> obtenerMiPerfil() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.obtenerPerfilActual(correo));
    }

}