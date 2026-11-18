package pe.com.nutrisalud.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.nutrisalud.api.model.dto.request.CargoRequest;
import pe.com.nutrisalud.api.model.dto.response.CargoResponse;
import pe.com.nutrisalud.api.service.ICargoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final ICargoService cargoService;

    // 1. Crear un nuevo cargo (SOLO ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CargoResponse> crearCargo(@Valid @RequestBody CargoRequest request) {
        return new ResponseEntity<>(cargoService.crearCargo(request), HttpStatus.CREATED);
    }

    // 2. Listar todos los cargos
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CargoResponse>> listarCargos() {
        return ResponseEntity.ok(cargoService.listarCargos());
    }
}