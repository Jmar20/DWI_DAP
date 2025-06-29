package org.utp.pydwi.alerta.application.internal.queryservices;

import org.utp.pydwi.alerta.domain.model.entities.Alerta;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.AlertaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertaQueryService {
    private final AlertaRepository repository;

    public List<Alerta> findAll() {
        return repository.findAll();
    }
    public Optional<Alerta> findById(Integer id) {
        return repository.findById(id);
    }
    public List<Alerta> findByTipo(String tipo) {
        return repository.findByTipo(tipo);
    }
    public List<Alerta> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        return repository.findByFechaBetween(desde, hasta);
    }
    public List<Alerta> findByActividadId(Integer actividadId) {
        return repository.findByActividadId(actividadId);
    }
}
