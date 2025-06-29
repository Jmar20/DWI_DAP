package org.utp.pydwi.gestioncultivo.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;

public interface CultivoJpaRepository extends JpaRepository<Cultivo, Integer> {
}
