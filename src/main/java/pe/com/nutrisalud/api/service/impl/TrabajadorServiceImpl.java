package pe.com.nutrisalud.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.request.EmpresaRequest;
import pe.com.nutrisalud.api.model.dto.request.TrabajadorRequest;
import pe.com.nutrisalud.api.model.dto.response.TrabajadorResponse;
import pe.com.nutrisalud.api.model.entity.Empresa;
import pe.com.nutrisalud.api.model.entity.Trabajador;
import pe.com.nutrisalud.api.repository.IEmpresaRepository;
import pe.com.nutrisalud.api.repository.ITrabajadorRepository;
import pe.com.nutrisalud.api.service.ITrabajadorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrabajadorServiceImpl implements ITrabajadorService {

    private final ITrabajadorRepository trabajadorRepo;
    private final IEmpresaRepository empresaRepo;

    @Override
    public TrabajadorResponse crearTrabajador(TrabajadorRequest trabajadorRequest) {
        // Validamos que el documento no exista
        if (trabajadorRepo.existsByDocumentoIdentidad(trabajadorRequest.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe un trabajador registrado con ese documento: " + trabajadorRequest.getDocumentoIdentidad());
        }

        // Validar que la empresa exista
        Empresa empresa = empresaRepo.findById(trabajadorRequest.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + trabajadorRequest.getEmpresaId()));

        Trabajador trabajador = Trabajador.builder()
                .nombres(trabajadorRequest.getNombres())
                .apellidos(trabajadorRequest.getApellidos())
                .tipoDocumento(trabajadorRequest.getTipoDocumento())
                .documentoIdentidad(trabajadorRequest.getDocumentoIdentidad())
                .correo(trabajadorRequest.getCorreo())
                .cargo(trabajadorRequest.getCargo())
                .area(trabajadorRequest.getArea())
                .empresa(empresa)
                .build();

        return mapToResponse(trabajadorRepo.save(trabajador));
    }

    @Override
    public List<TrabajadorResponse> listarTrabajadores() {
        return trabajadorRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TrabajadorResponse obtenerTrabajadorPorId(Long id) {
       Trabajador trabajador = trabajadorRepo.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + id));
        return mapToResponse(trabajador);
    }

    @Override
    public TrabajadorResponse actualizarTrabajador(Long id, TrabajadorRequest trabajadorRequest) {
        Trabajador trabajador = trabajadorRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado con ID: " + id));

        // Validar documento único si se intenta cambiar
        if (!trabajador.getDocumentoIdentidad().equals(trabajadorRequest.getDocumentoIdentidad()) &&
                trabajadorRepo.existsByDocumentoIdentidad(trabajadorRequest.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe otro trabajador con el documento: " + trabajadorRequest.getDocumentoIdentidad());
        }

        Empresa empresa = empresaRepo.findById(trabajadorRequest.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + trabajadorRequest.getEmpresaId()));

        trabajador.setNombres(trabajadorRequest.getNombres());
        trabajador.setApellidos(trabajadorRequest.getApellidos());
        trabajador.setTipoDocumento(trabajadorRequest.getTipoDocumento());
        trabajador.setDocumentoIdentidad(trabajadorRequest.getDocumentoIdentidad());
        trabajador.setCorreo(trabajadorRequest.getCorreo());
        trabajador.setCargo(trabajadorRequest.getCargo());
        trabajador.setArea(trabajadorRequest.getArea());
        trabajador.setEmpresa(empresa);

        return mapToResponse(trabajadorRepo.save(trabajador));
    }

    @Override
    public void eliminarTrabajador(Long id) {
        if (!trabajadorRepo.existsById(id)) {
            throw new ResourceNotFoundException("Trabajador no encontrado con ID: " + id);
        }
        trabajadorRepo.deleteById(id);
    }

    // Método auxiliar para mapear Entity a DTO
    private TrabajadorResponse mapToResponse(Trabajador trabajador) {
        return TrabajadorResponse.builder()
                .id(trabajador.getId())
                .nombres(trabajador.getNombres())
                .apellidos(trabajador.getApellidos())
                .tipoDocumento(trabajador.getTipoDocumento())
                .documentoIdentidad(trabajador.getDocumentoIdentidad())
                .correo(trabajador.getCorreo())
                .cargo(trabajador.getCargo())
                .area(trabajador.getArea())
                .empresaId(trabajador.getEmpresa().getId())
                .razonSocialEmpresa(trabajador.getEmpresa().getRazonSocial())
                .fechaRegistro(trabajador.getFechaRegistro())
                .build();
    }
}
