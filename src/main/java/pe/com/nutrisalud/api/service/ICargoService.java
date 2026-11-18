package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.CargoRequest;
import pe.com.nutrisalud.api.model.dto.response.CargoResponse;

import java.util.List;

public interface ICargoService {
    CargoResponse crearCargo(CargoRequest request);
    List<CargoResponse> listarCargos();
}
