package pe.com.nutrisalud.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.model.dto.request.CargoRequest;
import pe.com.nutrisalud.api.model.dto.response.CargoResponse;
import pe.com.nutrisalud.api.model.entity.Cargo;
import pe.com.nutrisalud.api.repository.ICargoRepository;
import pe.com.nutrisalud.api.service.ICargoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements ICargoService {

    private final ICargoRepository cargoRepository;

    @Override
    public CargoResponse crearCargo(CargoRequest request) {
        // Validar que el nombre del cargo no exista ya
        if (cargoRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cargo con el nombre: " + request.getNombre());
        }

        Cargo nuevoCargo = Cargo.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();

        return mapToResponse(cargoRepository.save(nuevoCargo));
    }

    @Override
    public List<CargoResponse> listarCargos() {
        return cargoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CargoResponse mapToResponse(Cargo cargo) {
        return CargoResponse.builder()
                .id(cargo.getId())
                .nombre(cargo.getNombre())
                .descripcion(cargo.getDescripcion())
                .build();
    }
}