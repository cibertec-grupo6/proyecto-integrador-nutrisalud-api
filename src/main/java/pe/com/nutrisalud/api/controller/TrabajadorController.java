package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.request.TrabajadorRequest;
import pe.com.nutrisalud.api.model.dto.response.TrabajadorResponse;
import pe.com.nutrisalud.api.service.ITrabajadorService;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    private final ITrabajadorService trabajadorService;

    public TrabajadorController(ITrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVALUADOR_SALUD')") // Admin o Evaluador pueden registrar
    public ResponseEntity<TrabajadorResponse> crearTrabajador(@Valid @RequestBody TrabajadorRequest request) {
        TrabajadorResponse nuevoTrabajador = trabajadorService.crearTrabajador(request);
        return new ResponseEntity<>(nuevoTrabajador, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrabajadorResponse>> listarTrabajadores() {
        return ResponseEntity.ok(trabajadorService.listarTrabajadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorResponse> obtenerTrabajadorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(trabajadorService.obtenerTrabajadorPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVALUADOR_SALUD')")
    public ResponseEntity<TrabajadorResponse> actualizarTrabajador(
            @PathVariable Long id,
            @Valid @RequestBody TrabajadorRequest request) {
        return ResponseEntity.ok(trabajadorService.actualizarTrabajador(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar
    public ResponseEntity<Void> eliminarTrabajador(@PathVariable Long id) {
        trabajadorService.eliminarTrabajador(id);
        return ResponseEntity.noContent().build();
    }
}