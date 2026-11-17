package pe.com.nutrisalud.api.util;

import pe.com.nutrisalud.api.model.enums.ClasificacionIMC;

public class IMCCalculator {

    // Fórmula: Peso (kg) / Talla (m)²
    public static double calcularIMC(double pesoKg, double tallaM){
        return pesoKg / (tallaM * tallaM);
    }

    //Criterios OMS
    public static ClasificacionIMC determinarClasificacionIMC(double imc){
        if(imc < 18.5){
            return ClasificacionIMC.BAJO_PESO;
        } else if (imc < 25.0) {
            return ClasificacionIMC.NORMAL;
        } else if (imc < 30.0) {
            return ClasificacionIMC.SOBREPESO;
        } else{
            return ClasificacionIMC.OBESIDAD;
        }

        }
    // Redondear a dos decimales para que se vea limpio en la BD y respuesta
    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

}
