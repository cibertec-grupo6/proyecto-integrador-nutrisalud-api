package pe.com.nutrisalud.api.service;

import pe.com.nutrisalud.api.model.dto.request.EmpresaRequest;
import pe.com.nutrisalud.api.model.dto.response.EmpresaResponse;
import pe.com.nutrisalud.api.model.entity.Empresa;

import java.util.List;

public interface IEmpresaService {

    EmpresaResponse crearEmpresa(EmpresaRequest empresaRequest);
    List<EmpresaResponse> listarEmpresas();
    EmpresaResponse obtenerEmpresaPorId(Long id);
    EmpresaResponse actualizarEmpresa(Long id, EmpresaRequest empresaRequest);
    void eliminarEmpresa(Long id);
}
