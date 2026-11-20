package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.EvaluacionIMCRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;

public interface IEvaluacionIMCService {
    EvaluacionIMCResponse registrarYCalcularIMC(EvaluacionIMCRequest evaluacionIMCRequest, String correoEvaluador);


    // 1. Solo cambia el estado a APROBADO
    EvaluacionIMCResponse aprobarEvaluacion(Long evaluacionId, String correoMedico);

    // 2. Genera PDF, envía correo y cambia el estado a ENVIADO
    EvaluacionIMCResponse enviarEvaluacion(Long evaluacionId, String correoMedico);

}
