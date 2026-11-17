package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.request.EmpresaRequest;
import pe.com.nutrisalud.api.model.dto.response.EmpresaResponse;
import pe.com.nutrisalud.api.service.IEmpresaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final IEmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERA')")
    public ResponseEntity<EmpresaResponse> crearEmpresa(@Valid @RequestBody EmpresaRequest request) {
        EmpresaResponse nuevaEmpresa = empresaService.crearEmpresa(request);
        return new ResponseEntity<>(nuevaEmpresa, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EmpresaResponse>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.listarEmpresas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmpresaResponse> obtenerEmpresaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obtenerEmpresaPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERA')")
    public ResponseEntity<EmpresaResponse> actualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(empresaService.actualizarEmpresa(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo el admin puede eliminar
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}