package pe.com.nutrisalud.api.model.enums;

public enum EstadoEvaluacion {
    PENDIENTE,   // Recién registrado por la enfermera
    APROBADO,    // Revisado y aprobado por el médico (aquí se envía el correo)
    OBSERVADO    // (Opcional) El médico pide que se vuelva a tomar la medida
}
