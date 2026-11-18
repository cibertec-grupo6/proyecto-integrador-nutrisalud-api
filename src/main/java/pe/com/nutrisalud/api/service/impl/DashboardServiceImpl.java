package pe.com.nutrisalud.api.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionSaltoResponse;
import pe.com.nutrisalud.api.model.dto.response.HistorialTrabajadorDTO;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;
import pe.com.nutrisalud.api.model.entity.Trabajador;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.repository.IEvaluacionIMCRepository;
import pe.com.nutrisalud.api.repository.IEvaluacionSaltoRepository;
import pe.com.nutrisalud.api.repository.ITrabajadorRepository;
import pe.com.nutrisalud.api.service.IDashboardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements IDashboardService {

    private final ITrabajadorRepository trabajadorRepository;
    private final IEvaluacionIMCRepository imcRepository;
    private final IEvaluacionSaltoRepository saltoRepository;


    @Override
    public HistorialTrabajadorDTO obtenerHistorialTrabajador(Long trabajadorId) {
        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado con ID: " + trabajadorId));

        // Obtener historiales ordenados por fecha
        List<EvaluacionIMC> listaIMC = imcRepository.findByTrabajadorIdOrderByFechaEvaluacionDesc(trabajadorId);
        List<EvaluacionSalto> listaSalto = saltoRepository.findByTrabajadorIdOrderByFechaEvaluacionDesc(trabajadorId);

        return HistorialTrabajadorDTO.builder()
                .trabajadorId(trabajador.getId())
                .nombreTrabajador(trabajador.getNombres() + " " + trabajador.getApellidos())
                .documentoIdentidad(trabajador.getDocumentoIdentidad())
                .nombreEmpresa(trabajador.getEmpresa().getRazonSocial())
                .cargoTrabajador(trabajador.getCargo())
                .historialIMC(listaIMC.stream().map(this::mapToIMCResponse).collect(Collectors.toList()))
                .historialSalto(listaSalto.stream().map(this::mapToSaltoResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Map<String, Object> obtenerEvaluacionesFiltradas(Long empresaId, EstadoEvaluacion estado) {
        Map<String, Object> resultado = new HashMap<>();

        // Si no se pasa empresa, retornamos vacío o podrías lanzar excepción.
        // Para el MVP, asumimos que el médico siempre filtra por empresa.
        List<EvaluacionIMC> imcs = imcRepository.filtrarPorEmpresaYEstado(empresaId, estado);
        List<EvaluacionSalto> saltos = saltoRepository.filtrarPorEmpresaYEstado(empresaId, estado);

        resultado.put("evaluacionesIMC", imcs.stream().map(this::mapToIMCResponse).collect(Collectors.toList()));
        resultado.put("evaluacionesSalto", saltos.stream().map(this::mapToSaltoResponse).collect(Collectors.toList()));
        resultado.put("totalPendientes", (estado == null || estado == EstadoEvaluacion.PENDIENTE) ? imcs.size() + saltos.size() : 0);

        return resultado;
    }

    // Mapeos internos (puedes reutilizar los de los otros services si los haces públicos, pero por encapsulamiento los dejo aquí)
    private EvaluacionIMCResponse mapToIMCResponse(EvaluacionIMC e) {
        return EvaluacionIMCResponse.builder()
                .id(e.getId())
                .trabajadorId(e.getTrabajador().getId())
                .nombreTrabajador(e.getTrabajador().getNombres() + " " + e.getTrabajador().getApellidos())
                .pesoKg(e.getPesoKg())
                .tallaM(e.getTallaM())
                .imcCalculado(e.getImcCalculado())
                .clasificacion(e.getClasificacion())
                .fechaEvaluacion(e.getFechaEvaluacion())
                .estado(e.getEstado())
                .tipoEvaluacion("IMC")
                .nombreEvaluador(e.getEvaluador().getNombres() + " " + e.getEvaluador().getApellidos())
                .cargoEvaluador(e.getEvaluador().getCargo().getNombre())
                .build();
    }

    private EvaluacionSaltoResponse mapToSaltoResponse(EvaluacionSalto e) {
        return EvaluacionSaltoResponse.builder()
                .id(e.getId())
                .trabajadorId(e.getTrabajador().getId())
                .nombreTrabajador(e.getTrabajador().getNombres() + " " + e.getTrabajador().getApellidos())
                .alcanceReposoCm(e.getAlcanceReposoCm())
                .alcanceSaltoCm(e.getAlcanceSaltoCm())
                .alturaSaltoCm(e.getAlturaSaltoCm())
                .clasificacionRendimiento(e.getClasificacionRendimiento())
                .fechaEvaluacion(e.getFechaEvaluacion())
                .estado(e.getEstado())
                .tipoEvaluacion("SALTO_VERTICAL")
                .nombreEvaluador(e.getEvaluador().getNombres() + " " + e.getEvaluador().getApellidos())
                .cargoEvaluador(e.getEvaluador().getCargo().getNombre())
                .build();
    }
}