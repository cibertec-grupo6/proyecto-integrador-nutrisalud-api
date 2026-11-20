package pe.com.nutrisalud.api.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import pe.com.nutrisalud.api.model.entity.EvaluacionIMC;
import pe.com.nutrisalud.api.model.entity.EvaluacionSalto;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generarPdfIMC(EvaluacionIMC evaluacion) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Encabezado
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("RESULTADO DE EVALUACIÓN MÉDICA OCUPACIONAL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // 2. Datos del Trabajador
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);

            String cargoTrabajador = (evaluacion.getTrabajador().getCargo() != null && !evaluacion.getTrabajador().getCargo().isBlank())
                    ? evaluacion.getTrabajador().getCargo()
                    : "No especificado";

            document.add(new Paragraph("DATOS DEL TRABAJADOR", headerFont));
            document.add(new Paragraph("Nombre: " + evaluacion.getTrabajador().getNombres() + " " + evaluacion.getTrabajador().getApellidos(), normalFont));
            document.add(new Paragraph("DNI: " + evaluacion.getTrabajador().getDocumentoIdentidad(), normalFont));
            document.add(new Paragraph("Empresa: " + evaluacion.getTrabajador().getEmpresa().getRazonSocial(), normalFont));
            document.add(new Paragraph("Cargo: " + cargoTrabajador, normalFont));
            document.add(Chunk.NEWLINE);

            // 3. Resultados de la Evaluación
            document.add(new Paragraph("RESULTADOS DE LA EVALUACIÓN (IMC)", headerFont));
            document.add(new Paragraph("Fecha de Evaluación: " + evaluacion.getFechaEvaluacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            document.add(new Paragraph("Peso: " + evaluacion.getPesoKg() + " kg", normalFont));
            document.add(new Paragraph("Talla: " + evaluacion.getTallaM() + " m", normalFont));

            // Resaltar el resultado
            Font resultFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLUE);
            document.add(new Paragraph("IMC Calculado: " + evaluacion.getImcCalculado(), resultFont));
            document.add(new Paragraph("Clasificación: " + evaluacion.getClasificacion().name().replace('_', ' '), resultFont));
            document.add(Chunk.NEWLINE);

            // 4. Pie de página
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);
            Paragraph footer = new Paragraph("Este documento es un resultado preliminar generado por el sistema NutriSalud.\nPara consultas, comuníquese con el departamento de Salud Ocupacional.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return out.toByteArray();
    }

    public byte[] generarPdfSaltoVertical(EvaluacionSalto evaluacion) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Encabezado
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("RESULTADO DE EVALUACIÓN FÍSICA - SALTO VERTICAL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // 2. Datos del Trabajador
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);

            String cargoTrabajador = (evaluacion.getTrabajador().getCargo() != null && !evaluacion.getTrabajador().getCargo().isBlank())
                    ? evaluacion.getTrabajador().getCargo()
                    : "No especificado";

            document.add(new Paragraph("DATOS DEL TRABAJADOR", headerFont));
            document.add(new Paragraph("Nombre: " + evaluacion.getTrabajador().getNombres() + " " + evaluacion.getTrabajador().getApellidos(), normalFont));
            document.add(new Paragraph("DNI: " + evaluacion.getTrabajador().getDocumentoIdentidad(), normalFont));
            document.add(new Paragraph("Empresa: " + evaluacion.getTrabajador().getEmpresa().getRazonSocial(), normalFont));
            document.add(new Paragraph("Cargo: " + cargoTrabajador, normalFont));
            document.add(Chunk.NEWLINE);

            // 3. Resultados de la Evaluación
            document.add(new Paragraph("RESULTADOS DE LA PRUEBA DE SARGENT", headerFont));
            document.add(new Paragraph("Fecha de Evaluación: " + evaluacion.getFechaEvaluacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            document.add(new Paragraph("Alcance en Reposo: " + evaluacion.getAlcanceReposoCm() + " cm", normalFont));
            document.add(new Paragraph("Alcance con Salto: " + evaluacion.getAlcanceSaltoCm() + " cm", normalFont));

            // Resaltar el resultado
            Font resultFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLUE);
            document.add(new Paragraph("Altura del Salto: " + evaluacion.getAlturaSaltoCm() + " cm", resultFont));
            document.add(new Paragraph("Clasificación de Rendimiento: " + evaluacion.getClasificacionRendimiento().name().replace('_', ' '), resultFont));
            document.add(Chunk.NEWLINE);

            // 4. Tabla de referencia
            document.add(new Paragraph("TABLA DE REFERENCIA (Test de Sargent)", headerFont));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(6f);

            table.addCell(new Phrase("Clasificación", headerFont));
            table.addCell(new Phrase("Altura (cm)", headerFont));

            table.addCell(new Phrase("Excelente", normalFont));
            table.addCell(new Phrase("Mayor a 60 cm", normalFont));
            table.addCell(new Phrase("Muy Bueno", normalFont));
            table.addCell(new Phrase("50 - 59 cm", normalFont));
            table.addCell(new Phrase("Bueno", normalFont));
            table.addCell(new Phrase("40 - 49 cm", normalFont));
            table.addCell(new Phrase("Regular", normalFont));
            table.addCell(new Phrase("30 - 39 cm", normalFont));
            table.addCell(new Phrase("Deficiente", normalFont));
            table.addCell(new Phrase("Menor a 30 cm", normalFont));

            document.add(table);
            document.add(Chunk.NEWLINE);

            // 5. Pie de página
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);
            Paragraph footer = new Paragraph("Este documento es un resultado preliminar generado por el sistema NutriSalud.\nPara consultas, comuníquese con el departamento de Salud Ocupacional.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF de Salto Vertical", e);
        }

        return out.toByteArray();
    }
}