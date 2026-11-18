package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.EvaluacionIMCRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;

public interface IEvaluacionIMCService {
    EvaluacionIMCResponse registrarYCalcularIMC(EvaluacionIMCRequest evaluacionIMCRequest, String correoEvaluador);

    EvaluacionIMCResponse aprobarEvaluacion(Long evaluacionId, String correoMedico);
}
