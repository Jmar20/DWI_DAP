package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    Optional<Direccion> findByParcelaId(Integer parcelaId);
}
