package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantaGestionCultivoRepository extends JpaRepository<Planta, Integer> {
}
