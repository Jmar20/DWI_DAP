package org.utp.pydwi.planificacion.application.internal.commandservices;

import org.utp.pydwi.planificacion.domain.model.entities.Planificacion;
import org.utp.pydwi.planificacion.infrastructure.persistence.jpa.PlanificacionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanificacionCommandService {
    private final PlanificacionRepository repository;

    public Planificacion save(Planificacion planificacion) {
        return repository.save(planificacion);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
