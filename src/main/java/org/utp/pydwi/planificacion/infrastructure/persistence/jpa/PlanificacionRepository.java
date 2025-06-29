package org.utp.pydwi.planificacion.infrastructure.persistence.jpa;

import org.utp.pydwi.planificacion.domain.model.entities.Planificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PlanificacionRepository extends JpaRepository<Planificacion, Integer> {
    Optional<Planificacion> findByCultivoId(Integer cultivoId);
    List<Planificacion> findAllByCultivoId(Integer cultivoId);
}
