package org.utp.pydwi.alerta.application.internal.commandservices;

import org.utp.pydwi.alerta.domain.model.entities.Notificacion;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.NotificacionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionCommandService {
    private final NotificacionRepository repository;

    public Notificacion save(Notificacion notificacion) {
        return repository.save(notificacion);
    }
}
