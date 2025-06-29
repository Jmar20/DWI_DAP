package org.utp.pydwi.planificacion.application.internal.queryservices;

import org.utp.pydwi.planificacion.domain.model.entities.Planificacion;
import org.utp.pydwi.planificacion.infrastructure.persistence.jpa.PlanificacionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanificacionQueryService {
    private final PlanificacionRepository repository;

    public List<Planificacion> findAll() {
        return repository.findAll();
    }

    public Optional<Planificacion> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Planificacion> findByCultivoId(Integer cultivoId) {
        return repository.findByCultivoId(cultivoId);
    }

    public List<Planificacion> findAllByCultivoId(Integer cultivoId) {
        return repository.findAllByCultivoId(cultivoId);
    }
}
