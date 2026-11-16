package pe.com.nutrisalud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.nutrisalud.api.model.entity.Trabajador;

import java.util.Optional;

public interface ITrabajadorRepository extends JpaRepository<Trabajador, Long> {

    //Verificar si el documento esta registrado
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
    Optional<Trabajador> findByDocumentoIdentidad(String documentoIdentidad);
}
