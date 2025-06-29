package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.springframework.stereotype.Service;

@Service
public class ActividadQueryService {
    public Actividad findUltimaActividadPorTipo(Integer cultivoId, String tipo) {
        // TODO: Implementar consulta real a la base de datos
        return null;
    }
}
