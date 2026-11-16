package pe.com.nutrisalud.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trabajador")
public class Trabajador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(name = "tipo_documento", length = 10)
    private String tipoDocumento; // ej: "DNI", "CE", "PAS"

    @Column(name = "documento_identidad", nullable = false,unique = true, length = 15)
    private String documentoIdentidad;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(length = 100)
    private String cargo;

    @Column(length = 100)
    private String area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;
}
