package pe.com.nutrisalud.api.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.nutrisalud.api.model.enums.Rol;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nombres;
	
	@Column(nullable = false, length = 100)
	private String apellidos;
	
	@Column(nullable = false, unique = true, length = 100)
	private String correo;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Rol rol;
	
	@Column(nullable = false)
	private Boolean activo;
	
	@Column(name = "fecha_creacion", updatable = false)
	private LocalDateTime fechaCreacion;
	
	protected void onCreate() {
		this.fechaCreacion = LocalDateTime.now();
		if(this.fechaCreacion == null) {
			this.activo = true;
		}
	}
}
