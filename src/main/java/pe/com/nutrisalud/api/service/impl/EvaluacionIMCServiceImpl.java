package pe.com.nutrisalud.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.exception.ResourceNotFoundException;
import pe.com.nutrisalud.api.model.dto.request.EvaluacionIMCRequest;
import pe.com.nutrisalud.api.model.dto.response.EvaluacionIMCResponse;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.entity.Trabajador;
import pe.com.nutrisalud.api.model.entity.Usuario;
import pe.com.nutrisalud.api.model.enums.ClasificacionIMC;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;
import pe.com.nutrisalud.api.model.enums.Rol;
import pe.com.nutrisalud.api.repository.IEvaluacionIMCRepository;
import pe.com.nutrisalud.api.repository.ITrabajadorRepository;
import pe.com.nutrisalud.api.repository.IUsuarioRepository;
import pe.com.nutrisalud.api.service.IEvaluacionIMCService;
import pe.com.nutrisalud.api.util.IMCCalculator;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EvaluacionIMCServiceImpl implements IEvaluacionIMCService {

    private final IEvaluacionIMCRepository evaluacionIMCRepo;
    private final ITrabajadorRepository trabajadorRepo;
    private final IUsuarioRepository usuarioRepo;

    @Override
    public EvaluacionIMCResponse registrarYCalcularIMC(EvaluacionIMCRequest evaluacionIMCRequest, String correoEvaluador) {
        // Validar si el trabjador existe
        Trabajador trabajador =  trabajadorRepo.findById(evaluacionIMCRequest.getTrabajadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado con Id: " + evaluacionIMCRequest.getTrabajadorId()));

        // Validar que el evaluador (usuario logueado) exista
        Usuario evaluador = usuarioRepo.findByCorreo(correoEvaluador)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario evaluador no encontrado con ID: " + correoEvaluador));

        // Calcular IMC y Clasificación (HU03)
        double imc = IMCCalculator.calcularIMC(evaluacionIMCRequest.getPesoKg(), evaluacionIMCRequest.getTallaM());
        double imcRedondeado = IMCCalculator.redondear(imc);
        ClasificacionIMC clasificacion = IMCCalculator.determinarClasificacionIMC(imcRedondeado);

        // Guardar en la base de datos
        EvaluacionIMC evaluacion = EvaluacionIMC.builder()
                .trabajador(trabajador)
                .pesoKg(evaluacionIMCRequest.getPesoKg())
                .tallaM(evaluacionIMCRequest.getTallaM())
                .imcCalculado(imcRedondeado)
                .clasificacion(clasificacion)
                .fechaEvaluacion(LocalDate.now())
                .evaluador(evaluador)
                .estado(EstadoEvaluacion.PENDIENTE)
                .build();

        EvaluacionIMC evaluacionGuardada = evaluacionIMCRepo.save(evaluacion);

        // Retornar la respuesta mapeada
        return mapToResponse(evaluacionGuardada);
    }

    @Override
    public EvaluacionIMCResponse aprobarEvaluacion(Long evaluacionId, String correoMedico) {
        // 1. Buscar la evaluación
        EvaluacionIMC evaluacion = evaluacionIMCRepo.findById(evaluacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada con ID: " + evaluacionId));

        // 2. Validar que el usuario que aprueba sea un MÉDICO (Seguridad a nivel de negocio)
        Usuario medico = usuarioRepo.findByCorreo(correoMedico)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (medico.getRol() != Rol.MEDICO) {
            throw new IllegalStateException("Solo un usuario con rol MÉDICO puede aprobar evaluaciones.");
        }

        // 3. Cambiar el estado a APROBADO
        evaluacion.setEstado(EstadoEvaluacion.APROBADO);

        // 4. Guardar los cambios
        EvaluacionIMC evaluacionAprobada = evaluacionIMCRepo.save(evaluacion);

        // TODO (Sprint 2): Aquí irá el disparador del servicio de envío de correos
        // emailService.enviarResultadoIMC(evaluacionAprobada);

        return mapToResponse(evaluacionAprobada);
    }


    //Metodo Auxiliar
    private EvaluacionIMCResponse mapToResponse(EvaluacionIMC evaluacion){
        return EvaluacionIMCResponse.builder()
                .id(evaluacion.getId())
                .trabajadorId(evaluacion.getTrabajador().getId())
                .nombreTrabajador(evaluacion.getTrabajador().getNombres() + " " + evaluacion.getTrabajador().getApellidos())
                .pesoKg(evaluacion.getPesoKg())
                .tallaM(evaluacion.getTallaM())
                .imcCalculado(evaluacion.getImcCalculado())
                .clasificacion(evaluacion.getClasificacion())
                .fechaEvaluacion(evaluacion.getFechaEvaluacion())
                .estado(evaluacion.getEstado())
                .tipoEvaluacion("IMC")
                .nombreEvaluador(evaluacion.getEvaluador().getNombres() + " " + evaluacion.getEvaluador().getApellidos())
                .build();
    }
}
