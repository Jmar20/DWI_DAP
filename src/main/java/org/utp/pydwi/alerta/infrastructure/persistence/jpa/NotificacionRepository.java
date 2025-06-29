package org.utp.pydwi.alerta.infrastructure.persistence.jpa;

import org.utp.pydwi.alerta.domain.model.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuarioId(Integer usuarioId);
    List<Notificacion> findByAlertaId(Integer alertaId);
}
