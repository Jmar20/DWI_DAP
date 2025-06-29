package org.utp.pydwi.alerta.application.internal.queryservices;

import org.utp.pydwi.alerta.domain.model.entities.Notificacion;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.NotificacionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionQueryService {
    private final NotificacionRepository repository;

    public List<Notificacion> findByUsuarioId(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
    public List<Notificacion> findByAlertaId(Integer alertaId) {
        return repository.findByAlertaId(alertaId);
    }
}
