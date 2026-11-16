package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.TrabajadorRequest;
import pe.com.nutrisalud.api.model.dto.response.TrabajadorResponse;

import java.util.List;

public interface ITrabajadorService {
    TrabajadorResponse crearTrabajador(TrabajadorRequest trabajadorRequest);
    List<TrabajadorResponse> listarTrabajadores();
    TrabajadorResponse obtenerTrabajadorPorId(Long id);
    TrabajadorResponse actualizarTrabajador(Long id, TrabajadorRequest trabajadorRequest);
    void eliminarTrabajador(Long id);
}
