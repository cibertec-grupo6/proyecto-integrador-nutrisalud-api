package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.request.EvaluacionSaltoRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionSaltoResponse;
import pe.com.nutrisalud.api.service.IEvaluacionSaltoService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/evaluaciones/salto")
public class EvaluacionSaltoController {

    private final IEvaluacionSaltoService evaluacionSaltoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERA')")
    public ResponseEntity<EvaluacionSaltoResponse> registrarEvaluacion(@Valid @RequestBody EvaluacionSaltoRequest request) {
        String correoEvaluador = SecurityContextHolder.getContext().getAuthentication().getName();
        EvaluacionSaltoResponse respuesta = evaluacionSaltoService.registrarYCalcularSalto(request, correoEvaluador);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<EvaluacionSaltoResponse> aprobarEvaluacion(@PathVariable Long id) {
        String correoMedico = SecurityContextHolder.getContext().getAuthentication().getName();
        EvaluacionSaltoResponse respuesta = evaluacionSaltoService.aprobarEvaluacion(id, correoMedico);
        return ResponseEntity.ok(respuesta);
    }
}