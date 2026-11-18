package pe.com.nutrisalud.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pe.com.nutrisalud.api.model.enums.ClasificacionSalto;
import pe.com.nutrisalud.api.model.enums.EstadoEvaluacion;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "evaluacion_salto")
public class EvaluacionSalto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Trabajador trabajador;

    @Column(name = "alcance_reposo_cm", nullable = false)
    private Double alcanceReposoCm;

    @Column(name = "alcance_salto_cm", nullable = false)
    private Double alcanceSaltoCm;

    @Column(name = "altura_salto_cm", nullable = false)
    private Double alturaSaltoCm; // Calculado: alcance_salto - alcance_reposo

    @Enumerated(EnumType.STRING)
    @Column(name = "clasificacion_rendimiento", nullable = false, length = 20)
    private ClasificacionSalto clasificacionRendimiento;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluador_id")
    private Usuario evaluador;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEvaluacion estado;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDate fechaRegistro;
}