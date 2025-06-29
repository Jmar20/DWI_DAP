package org.utp.pydwi.alerta.application.internal.commandservices;

import org.utp.pydwi.alerta.domain.model.entities.Alerta;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.AlertaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertaCommandService {
    private final AlertaRepository repository;

    public Alerta save(Alerta alerta) {
        return repository.save(alerta);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
