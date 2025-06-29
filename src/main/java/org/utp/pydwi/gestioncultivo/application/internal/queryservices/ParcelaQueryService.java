package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParcelaQueryService {
    public List<Parcela> findAll() {
        // TODO: Implementar consulta real a la base de datos
        return List.of();
    }
}
