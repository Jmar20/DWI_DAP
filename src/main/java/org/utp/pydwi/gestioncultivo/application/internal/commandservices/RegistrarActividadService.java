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
                .nombre(command.getNombre())
                .nombreActividad(command.getNombre()) // Llenar ambos campos con el mismo valor
                .descripcion(command.getDescripcion())
                .fechaEjecucion(command.getFechaEjecucion())
                .prioridad(command.getPrioridad())
                .realizada(command.getRealizada() != null ? command.getRealizada() : false)
                .cultivo(cultivo)
                .userId(command.getUserId())
                .cultivoId(cultivo.getId()) // Forzar el id explícitamente
                .build();
        Actividad saved = actividadRepository.save(actividad);
        actividadRepository.flush();
        return saved;
    }

    public Actividad actualizarActividad(ActualizarActividadCommand command) {
        Actividad actividadExistente = actividadRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));
        
        Cultivo cultivo = cultivoRepository.findById(command.getCultivoId())
                .orElseThrow(() -> new IllegalArgumentException("Cultivo no encontrado"));

        // Actualizar campos
        actividadExistente.setNombre(command.getNombre());
        actividadExistente.setNombreActividad(command.getNombre()); // Llenar ambos campos
        actividadExistente.setDescripcion(command.getDescripcion());
        actividadExistente.setFechaEjecucion(command.getFechaEjecucion());
        actividadExistente.setPrioridad(command.getPrioridad());
        actividadExistente.setRealizada(command.getRealizada() != null ? command.getRealizada() : false);
        actividadExistente.setCultivo(cultivo);
        actividadExistente.setUserId(command.getUserId());

        Actividad updated = actividadRepository.save(actividadExistente);
        actividadRepository.flush();
        return updated;
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
