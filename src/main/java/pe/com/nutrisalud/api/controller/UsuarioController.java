package pe.com.nutrisalud.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.com.nutrisalud.api.model.dto.request.CambiarPasswordRequest;
import pe.com.nutrisalud.api.model.dto.request.ResetPasswordRequest;
import pe.com.nutrisalud.api.service.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. El usuario cambia su propia contraseña (requiere estar logueado)
    @PutMapping("/mi-password")
    public ResponseEntity<String> cambiarMiPassword(@Valid @RequestBody CambiarPasswordRequest request) {
        // Obtenemos el correo del usuario logueado desde el contexto de seguridad
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        
        usuarioService.cambiarMiPassword(correo, request.getPasswordActual(), request.getPasswordNueva());
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    // 2. Solo el ADMIN puede resetear la contraseña de otro usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<String> resetearPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        usuarioService.resetearPasswordPorAdmin(id, request.getNuevaPassword());
        return ResponseEntity.ok("Contraseña del usuario reseteada exitosamente");
    }
}