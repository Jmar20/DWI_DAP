package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ActividadRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.CultivoGestionCultivoRepository;

@Service
@RequiredArgsConstructor
public class RegistrarActividadService {
    private final ActividadRepository actividadRepository;
    private final CultivoGestionCultivoRepository cultivoRepository;

    public Actividad registrarActividad(RegistrarActividadCommand command) {
        Cultivo cultivo = cultivoRepository.findById(command.getCultivoId())
                .orElseThrow(() -> new IllegalArgumentException("Cultivo no encontrado"));
        Actividad actividad = Actividad.builder()
                .tipo(command.getTipo())
                .fecha(command.getFecha())
                .cultivo(cultivo)
                .build();
        return actividadRepository.save(actividad);
    }

    public Actividad registrarActividad(Integer cultivoId, RegistrarActividadCommand command) {
        // TODO: Implementar lógica real
        return null;
    }

    public Actividad marcarComoRealizada(Integer actividadId) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElse(null);
        if (actividad == null) return null;
        actividad.setRealizada(true);
        return actividadRepository.save(actividad);
    }
}
