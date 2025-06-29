package org.utp.pydwi.planificacion.infrastructure.persistence.jpa;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlantaRepository extends JpaRepository<Planta, Integer> {
    Optional<Planta> findById(Integer id);
}
