package pe.com.nutrisalud.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.request.EmpresaRequest;
import pe.com.nutrisalud.api.model.dto.response.EmpresaResponse;
import pe.com.nutrisalud.api.model.entity.Empresa;
import pe.com.nutrisalud.api.repository.IEmpresaRepository;
import pe.com.nutrisalud.api.service.IEmpresaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements IEmpresaService {

    private final IEmpresaRepository empresaRepo;

    @Override
    public EmpresaResponse crearEmpresa(EmpresaRequest empresaRequest) {

        //Antes de crear validamos si la empresa existe
        if(empresaRepo.existsByRuc(empresaRequest.getRuc())){
            throw new IllegalArgumentException("Ya existe una empresa registrada");
        }
        Empresa empresa = Empresa.builder()
                .razonSocial(empresaRequest.getRazonSocial())
                .ruc(empresaRequest.getRuc())
                .sector(empresaRequest.getSector())
                .direccion(empresaRequest.getDireccion())
                .correoContacto(empresaRequest.getCorreoContacto())
                .telefono(empresaRequest.getTelefono())
                .build();
        Empresa empresaGuardada = empresaRepo.save(empresa);
        return mapToResponse(empresaGuardada);
    }

    @Override
    public List<EmpresaResponse> listarEmpresas() {
        return empresaRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmpresaResponse obtenerEmpresaPorId(Long id) {
        Empresa empresa = empresaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));
        return mapToResponse(empresa);
    }

    @Override
    public EmpresaResponse actualizarEmpresa(Long id, EmpresaRequest empresaRequest) {
        Empresa empresa = empresaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        // Validar RUC único (si se cambió el RUC)
        if (!empresa.getRuc().equals(empresaRequest.getRuc()) && empresaRepo.existsByRuc(empresaRequest.getRuc())) {
            throw new IllegalArgumentException("Ya existe otra empresa registrada con el RUC: " + empresaRequest.getRuc());
        }

        empresa.setRazonSocial(empresaRequest.getRazonSocial());
        empresa.setRuc(empresaRequest.getRuc());
        empresa.setSector(empresaRequest.getSector());
        empresa.setDireccion(empresaRequest.getDireccion());
        empresa.setCorreoContacto(empresaRequest.getCorreoContacto());
        empresa.setTelefono(empresaRequest.getTelefono());

        return mapToResponse(empresaRepo.save(empresa));
    }

    @Override
    public void eliminarEmpresa(Long id) {
        if (!empresaRepo.existsById(id)) {
            throw new ResourceNotFoundException("Empresa no encontrada con ID: " + id);
        }
        empresaRepo.deleteById(id);
    }

    //Metodo auxiliar para mapear Entity a DTO
    private EmpresaResponse mapToResponse(Empresa empresa){
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .razonSocial(empresa.getRazonSocial())
                .ruc(empresa.getRuc())
                .sector(empresa.getSector())
                .direccion(empresa.getDireccion())
                .correoContacto(empresa.getCorreoContacto())
                .telefono(empresa.getTelefono())
                .fechaRegistro(empresa.getFechaRegistro())
                .build();
    }
}
