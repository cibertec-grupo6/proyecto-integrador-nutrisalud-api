package pe.com.nutrisalud.api.util;

import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;

public class SaltoVerticalCalculator {

    // Fórmula del Test de Sargent: Altura del salto = Alcance con salto - Alcance en reposo
    public static double calcularAlturaSalto(double alcanceSaltoCm, double alcanceReposoCm) {
        return alcanceSaltoCm - alcanceReposoCm;
    }

    // Clasificación simplificada basada en normas estándar (puedes ajustarla según los criterios exactos de NutriSalud)
    // Valores en cm para un adulto promedio.
    public static ClasificacionSalto determinarClasificacion(double alturaSaltoCm) {
        if (alturaSaltoCm < 30.0) {
            return ClasificacionSalto.DEFICIENTE;
        } else if (alturaSaltoCm < 40.0) {
            return ClasificacionSalto.REGULAR;
        } else if (alturaSaltoCm < 50.0) {
            return ClasificacionSalto.BUENO;
        } else if (alturaSaltoCm < 60.0) {
            return ClasificacionSalto.MUY_BUENO;
        } else {
            return ClasificacionSalto.EXCELENTE;
        }
    }

    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}