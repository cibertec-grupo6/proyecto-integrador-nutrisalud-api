package pe.com.nutrisalud.api.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.response.*;
import pe.com.nutrisalud.api.model.entity.Empresa;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;
import pe.com.nutrisalud.api.model.entity.Trabajador;
import pe.com.nutrisalud.api.model.enums.ClasificacionIMC;
import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.repository.IEmpresaRepository;
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

    private final IEmpresaRepository empresaRepository;


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

    @Override
    public ResumenDashboardDTO obtenerResumenGeneral() {
        return ResumenDashboardDTO.builder()
                .totalTrabajadores(trabajadorRepository.count())
                .totalEmpresas(empresaRepository.count())
                .evaluacionesPendientes(imcRepository.contarPorEstado(EstadoEvaluacion.PENDIENTE) +
                        saltoRepository.contarPorEstado(EstadoEvaluacion.PENDIENTE))
                .evaluacionesAprobadas(imcRepository.contarPorEstado(EstadoEvaluacion.APROBADO) +
                        saltoRepository.contarPorEstado(EstadoEvaluacion.APROBADO))
                .evaluacionesEnviadas(imcRepository.contarPorEstado(EstadoEvaluacion.ENVIADO) +
                        saltoRepository.contarPorEstado(EstadoEvaluacion.ENVIADO))
                .evaluacionesHoy(imcRepository.contarEvaluacionesHoy() +
                        saltoRepository.contarEvaluacionesHoy())
                .build();
    }

    @Override
    public EstadisticasIMCEmpresaDTO obtenerEstadisticasIMCPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + empresaId));

        // Construir el mapa de distribución
        Map<String, Integer> distribucion = new HashMap<>();
        distribucion.put("BAJO_PESO", imcRepository.contarPorClasificacion(empresaId, ClasificacionIMC.BAJO_PESO).intValue());
        distribucion.put("NORMAL", imcRepository.contarPorClasificacion(empresaId, ClasificacionIMC.NORMAL).intValue());
        distribucion.put("SOBREPESO", imcRepository.contarPorClasificacion(empresaId, ClasificacionIMC.SOBREPESO).intValue());
        distribucion.put("OBESIDAD", imcRepository.contarPorClasificacion(empresaId, ClasificacionIMC.OBESIDAD).intValue());

        return EstadisticasIMCEmpresaDTO.builder()
                .empresaId(empresaId)
                .nombreEmpresa(empresa.getRazonSocial())
                .totalEvaluaciones(imcRepository.contarEvaluacionesPorEmpresa(empresaId).intValue())
                .imcPromedio(redondear(imcRepository.calcularIMCPromedioPorEmpresa(empresaId)))
                .imcMinimo(imcRepository.calcularIMCMinimoPorEmpresa(empresaId))
                .imcMaximo(imcRepository.calcularIMCMaximoPorEmpresa(empresaId))
                .distribucionClasificacion(distribucion)
                .build();
    }

    @Override
    public EstadisticasSaltoEmpresaDTO obtenerEstadisticasSaltoPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + empresaId));

        Map<String, Integer> distribucion = new HashMap<>();
        distribucion.put("DEFICIENTE", saltoRepository.contarPorClasificacion(empresaId, ClasificacionSalto.DEFICIENTE).intValue());
        distribucion.put("REGULAR", saltoRepository.contarPorClasificacion(empresaId, ClasificacionSalto.REGULAR).intValue());
        distribucion.put("BUENO", saltoRepository.contarPorClasificacion(empresaId, ClasificacionSalto.BUENO).intValue());
        distribucion.put("MUY_BUENO", saltoRepository.contarPorClasificacion(empresaId, ClasificacionSalto.MUY_BUENO).intValue());
        distribucion.put("EXCELENTE", saltoRepository.contarPorClasificacion(empresaId, ClasificacionSalto.EXCELENTE).intValue());

        return EstadisticasSaltoEmpresaDTO.builder()
                .empresaId(empresaId)
                .nombreEmpresa(empresa.getRazonSocial())
                .totalEvaluaciones(saltoRepository.contarEvaluacionesPorEmpresa(empresaId).intValue())
                .alturaPromedioCm(redondear(saltoRepository.calcularAlturaPromedioPorEmpresa(empresaId)))
                .alturaMinimaCm(saltoRepository.calcularAlturaMinimaPorEmpresa(empresaId))
                .alturaMaximaCm(saltoRepository.calcularAlturaMaximaPorEmpresa(empresaId))
                .distribucionRendimiento(distribucion)
                .build();
    }

    private Double redondear(Double valor) {
        return valor != null ? Math.round(valor * 100.0) / 100.0 : 0.0;
    }

    @Override
    public EvolucionTrabajadorDTO obtenerEvolucionTrabajador(Long trabajadorId) {
        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado con ID: " + trabajadorId));

        // 1. Obtener historiales completos (ya ordenados por fecha DESC)
        List<EvaluacionIMC> historialIMC = imcRepository.findByTrabajadorIdOrderByFechaEvaluacionDesc(trabajadorId);
        List<EvaluacionSalto> historialSalto = saltoRepository.findByTrabajadorIdOrderByFechaEvaluacionDesc(trabajadorId);

        // 2. Obtener últimas evaluaciones
        EvaluacionIMC ultimaIMC = imcRepository.encontrarUltimaEvaluacionIMC(trabajadorId);
        EvaluacionSalto ultimoSalto = saltoRepository.encontrarUltimaEvaluacionSalto(trabajadorId);

        // 3. Construir puntos para el gráfico de líneas (invertir para que vayan cronológicamente)
        List<EvolucionTrabajadorDTO.PuntoEvolucion> puntosIMC = historialIMC.stream()
                .map(e -> EvolucionTrabajadorDTO.PuntoEvolucion.builder()
                        .fecha(e.getFechaEvaluacion())
                        .valor(e.getImcCalculado())
                        .clasificacion(e.getClasificacion().name())
                        .build())
                .collect(java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toList(),
                        list -> { java.util.Collections.reverse(list); return list; }
                ));

        List<EvolucionTrabajadorDTO.PuntoEvolucion> puntosSalto = historialSalto.stream()
                .map(e -> EvolucionTrabajadorDTO.PuntoEvolucion.builder()
                        .fecha(e.getFechaEvaluacion())
                        .valor(e.getAlturaSaltoCm())
                        .clasificacion(e.getClasificacionRendimiento().name())
                        .build())
                .collect(java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toList(),
                        list -> { java.util.Collections.reverse(list); return list; }
                ));

        // 4. Generar alertas clínicas automáticas
        List<String> alertas = new java.util.ArrayList<>();
        if (ultimaIMC != null) {
            if (ultimaIMC.getClasificacion() == ClasificacionIMC.OBESIDAD) {
                alertas.add("⚠️ El trabajador se encuentra en rango de OBESIDAD. Se recomienda seguimiento nutricional.");
            }
            if (ultimaIMC.getClasificacion() == ClasificacionIMC.BAJO_PESO) {
                alertas.add("⚠️ El trabajador se encuentra en rango de BAJO PESO. Se recomienda evaluación nutricional.");
            }
        }
        if (ultimoSalto != null && ultimoSalto.getClasificacionRendimiento() == ClasificacionSalto.DEFICIENTE) {
            alertas.add("⚠️ Rendimiento físico DEFICIENTE en prueba de salto vertical.");
        }

        // 5. Construir el DTO final
        return EvolucionTrabajadorDTO.builder()
                .trabajadorId(trabajador.getId())
                .nombreTrabajador(trabajador.getNombres() + " " + trabajador.getApellidos())
                .documentoIdentidad(trabajador.getDocumentoIdentidad())
                .nombreEmpresa(trabajador.getEmpresa().getRazonSocial())
                .totalEvaluacionesIMC(imcRepository.contarEvaluacionesTrabajador(trabajadorId).intValue())
                .imcPromedio(redondear(imcRepository.calcularIMCPromedioTrabajador(trabajadorId)))
                .imcActual(ultimaIMC != null ? ultimaIMC.getImcCalculado() : null)
                .clasificacionActual(ultimaIMC != null ? ultimaIMC.getClasificacion().name() : null)
                .totalEvaluacionesSalto(saltoRepository.contarEvaluacionesTrabajador(trabajadorId).intValue())
                .alturaSaltoPromedioCm(redondear(saltoRepository.calcularAlturaPromedioTrabajador(trabajadorId)))
                .alturaSaltoActualCm(ultimoSalto != null ? ultimoSalto.getAlturaSaltoCm() : null)
                .clasificacionSaltoActual(ultimoSalto != null ? ultimoSalto.getClasificacionRendimiento().name() : null)
                .evolucionIMC(puntosIMC)
                .evolucionSalto(puntosSalto)
                .alertas(alertas)
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