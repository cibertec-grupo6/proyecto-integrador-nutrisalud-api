package pe.com.nutrisalud.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.request.EvaluacionSaltoRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionSaltoResponse;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;
import pe.com.nutrisalud.api.model.entity.Trabajador;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.model.enums.Rol;
import pe.com.nutrisalud.api.repository.IEvaluacionSaltoRepository;
import pe.com.nutrisalud.api.repository.ITrabajadorRepository;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;
import pe.com.nutrisalud.api.service.IEvaluacionSaltoService;
import pe.com.nutrisalud.api.util.SaltoVerticalCalculator;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class EvaluacionSaltoServiceImpl implements IEvaluacionSaltoService {

    private final IEvaluacionSaltoRepository evaluacionSaltoRepository;
    private final ITrabajadorRepository trabajadorRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    public EvaluacionSaltoResponse registrarYCalcularSalto(EvaluacionSaltoRequest request, String correoEvaluador) {
        Trabajador trabajador = trabajadorRepository.findById(request.getTrabajadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado con ID: " + request.getTrabajadorId()));

        Usuario evaluador = usuarioRepository.findByCorreo(correoEvaluador)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario evaluador no encontrado con el correo: " + correoEvaluador));

        // Validar lógica de negocio: el alcance con salto debe ser mayor o igual al de reposo
        if (request.getAlcanceSaltoCm() < request.getAlcanceReposoCm()) {
            throw new IllegalArgumentException("El alcance con salto no puede ser menor que el alcance en reposo.");
        }

        double alturaSalto = SaltoVerticalCalculator.calcularAlturaSalto(request.getAlcanceSaltoCm(), request.getAlcanceReposoCm());
        double alturaRedondeada = SaltoVerticalCalculator.redondear(alturaSalto);
        ClasificacionSalto clasificacion = SaltoVerticalCalculator.determinarClasificacion(alturaRedondeada);

        EvaluacionSalto evaluacion = EvaluacionSalto.builder()
                .trabajador(trabajador)
                .alcanceReposoCm(request.getAlcanceReposoCm())
                .alcanceSaltoCm(request.getAlcanceSaltoCm())
                .alturaSaltoCm(alturaRedondeada)
                .clasificacionRendimiento(clasificacion)
                .fechaEvaluacion(LocalDate.now())
                .evaluador(evaluador)
                .estado(EstadoEvaluacion.PENDIENTE)
                .build();

        return mapToResponse(evaluacionSaltoRepository.save(evaluacion));
    }

    @Override
    public EvaluacionSaltoResponse aprobarEvaluacion(Long evaluacionId, String correoMedico) {
        EvaluacionSalto evaluacion = evaluacionSaltoRepository.findById(evaluacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación de salto no encontrada con ID: " + evaluacionId));

        Usuario medico = usuarioRepository.findByCorreo(correoMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (medico.getRol() != Rol.MEDICO) {
            throw new IllegalStateException("Solo un usuario con rol MÉDICO puede aprobar evaluaciones.");
        }

        evaluacion.setEstado(EstadoEvaluacion.APROBADO);
        return mapToResponse(evaluacionSaltoRepository.save(evaluacion));
    }

    private EvaluacionSaltoResponse mapToResponse(EvaluacionSalto evaluacion) {
        return EvaluacionSaltoResponse.builder()
                .id(evaluacion.getId())
                .trabajadorId(evaluacion.getTrabajador().getId())
                .nombreTrabajador(evaluacion.getTrabajador().getNombres() + " " + evaluacion.getTrabajador().getApellidos())
                .alcanceReposoCm(evaluacion.getAlcanceReposoCm())
                .alcanceSaltoCm(evaluacion.getAlcanceSaltoCm())
                .alturaSaltoCm(evaluacion.getAlturaSaltoCm())
                .clasificacionRendimiento(evaluacion.getClasificacionRendimiento())
                .fechaEvaluacion(evaluacion.getFechaEvaluacion())
                .estado(evaluacion.getEstado())
                .tipoEvaluacion("SALTO_VERTICAL")
                .nombreEvaluador(evaluacion.getEvaluador().getNombres() + " " + evaluacion.getEvaluador().getApellidos())
                .cargoEvaluador(evaluacion.getEvaluador().getCargo().getNombre())
                .build();
    }
}