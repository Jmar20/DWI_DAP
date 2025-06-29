package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ActividadRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarActividadesPorCultivoService {
    private final ActividadRepository actividadRepository;

    public List<Actividad> obtenerActividadesPorCultivo(Integer cultivoId) {
        return actividadRepository.findByCultivoId(cultivoId);
    }
}
