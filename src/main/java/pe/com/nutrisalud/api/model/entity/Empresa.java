package pe.com.nutrisalud.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.com.nutrisalud.api.model.dto.response.EmpresaResponse;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "razon_social",nullable = false, length = 150)
    private String razonSocial;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(length = 100)
    private String sector;

    @Column(length = 200)
    private String direccion;

    @Column(name = "correo_contacto", length = 100)
    private String correoContacto;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void  onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
