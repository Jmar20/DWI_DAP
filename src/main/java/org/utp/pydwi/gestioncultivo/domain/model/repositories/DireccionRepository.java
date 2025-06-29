package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
}
