package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.request.EvaluacionIMCRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;
import pe.com.nutrisalud.api.repository.IEvaluacionIMCRepository;
import pe.com.nutrisalud.api.service.IEvaluacionIMCService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluaciones/imc")
public class EvaluacionIMCController {

    private final IEvaluacionIMCService evaluacionIMCService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ENFERMERA')")
    public ResponseEntity<EvaluacionIMCResponse> registrarEvaluacion(@Valid @RequestBody EvaluacionIMCRequest evaluacionIMCRequest){

        String correoEvaluador = SecurityContextHolder.getContext().getAuthentication().getName();
        EvaluacionIMCResponse respuesta = evaluacionIMCService.registrarYCalcularIMC(evaluacionIMCRequest, correoEvaluador);

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);

    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('MEDICO')") // <-- SOLO EL MÉDICO PUEDE USAR ESTO
    public ResponseEntity<EvaluacionIMCResponse> aprobarEvaluacion(@PathVariable Long id) {
        // Obtener el correo del médico logueado desde el token JWT
        String correoMedico = SecurityContextHolder.getContext().getAuthentication().getName();

        EvaluacionIMCResponse respuesta = evaluacionIMCService.aprobarEvaluacion(id, correoMedico);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/enviar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<EvaluacionIMCResponse> enviarEvaluacion(@PathVariable Long id) {
        String correoMedico = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(evaluacionIMCService.enviarEvaluacion(id, correoMedico));
    }
}
