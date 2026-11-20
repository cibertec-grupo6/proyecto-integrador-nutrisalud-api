package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.EvaluacionSaltoRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionSaltoResponse;

public interface IEvaluacionSaltoService {
    EvaluacionSaltoResponse registrarYCalcularSalto(EvaluacionSaltoRequest request, String correoEvaluador);
    EvaluacionSaltoResponse aprobarEvaluacion(Long evaluacionId, String correoMedico);
    EvaluacionSaltoResponse enviarEvaluacion(Long evaluacionId, String correoMedico);
}
