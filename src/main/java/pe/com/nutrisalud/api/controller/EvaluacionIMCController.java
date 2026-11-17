package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.nutrisalud.api.model.dto.request.EvaluacionIMCRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;
import pe.com.nutrisalud.api.repository.IEvaluacionIMCRepository;
import pe.com.nutrisalud.api.service.IEvaluacionIMCService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluaciones")
public class EvaluacionIMCController {

    private final IEvaluacionIMCService evaluacionIMCService;

    @PostMapping("/imc")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENFERMERA')")
    public ResponseEntity<EvaluacionIMCResponse> registrarEvaluacion(@Valid @RequestBody EvaluacionIMCRequest evaluacionIMCRequest){

        // Extraer el correo del usuario logueado
        String correoEvaluador = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Delegar toda la lógica de negocio (incluyendo buscar al usuario) al Service
        EvaluacionIMCResponse respuesta = evaluacionIMCService.registrarYCalcularIMC(evaluacionIMCRequest, correoEvaluador);

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);

    }
}
