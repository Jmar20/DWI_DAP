package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadService;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.ActualizarActividadCommand;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ConsultarActividadesPorCultivoService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.interfaces.rest.resources.ActividadDTO;
import org.utp.pydwi.gestioncultivo.interfaces.rest.resources.ActualizarActividadRequest;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gestioncultivo/actividades")
@RequiredArgsConstructor
public class ActividadController {
    private final RegistrarActividadService registrarActividadService;
    private final ConsultarActividadesPorCultivoService consultarActividadesPorCultivoService;

    // POST - Crear nueva actividad
    @PostMapping
    public ResponseEntity<ActividadDTO> registrarActividad(@Valid @RequestBody RegistrarActividadCommand command) {
        Actividad actividad = registrarActividadService.registrarActividad(command);
        return ResponseEntity.ok(toDTO(actividad));
    }

    // POST - Crear actividad usando el id del cultivo en la URL
    @PostMapping("/cultivo/{cultivoId}")
    public ResponseEntity<ActividadDTO> registrarActividadPorCultivo(@PathVariable Integer cultivoId, @Valid @RequestBody RegistrarActividadCommand command) {
        command.setCultivoId(cultivoId);
        Actividad actividad = registrarActividadService.registrarActividad(command);
        return ResponseEntity.ok(toDTO(actividad));
    }

    // GET - Obtener actividades por cultivo
    @GetMapping("/cultivo/{cultivoId}")
    public ResponseEntity<List<ActividadDTO>> obtenerActividadesPorCultivo(@PathVariable Integer cultivoId) {
        List<Actividad> actividades = consultarActividadesPorCultivoService.obtenerActividadesPorCultivo(cultivoId);
        List<ActividadDTO> dtos = actividades.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    // GET - Obtener actividades por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ActividadDTO>> obtenerActividadesPorUsuario(@PathVariable Integer usuarioId) {
        List<Actividad> actividades = consultarActividadesPorCultivoService.obtenerActividadesPorUsuario(usuarioId);
        List<ActividadDTO> dtos = actividades.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    // PUT - Actualizar actividad completa
    @PutMapping("/{actividadId}")
    public ResponseEntity<ActividadDTO> actualizarActividad(@PathVariable Integer actividadId, @Valid @RequestBody ActualizarActividadRequest request) {
        try {
            ActualizarActividadCommand command = new ActualizarActividadCommand();
            command.setId(actividadId);
            command.setNombre(request.getNombre());
            command.setDescripcion(request.getDescripcion());
            command.setFechaEjecucion(request.getFechaEjecucion());
            command.setPrioridad(request.getPrioridad());
            command.setRealizada(request.getRealizada());
            command.setCultivoId(request.getCultivoId());
            command.setUserId(request.getUserId());
            
            Actividad actividad = registrarActividadService.actualizarActividad(command);
            return ResponseEntity.ok(toDTO(actividad));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Marcar actividad como realizada
    @PutMapping("/{actividadId}/realizada")
    public ResponseEntity<ActividadDTO> marcarActividadComoRealizada(@PathVariable Integer actividadId) {
        Actividad actividad = registrarActividadService.marcarComoRealizada(actividadId);
        if (actividad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(actividad));
    }

    private ActividadDTO toDTO(Actividad actividad) {
        ActividadDTO dto = new ActividadDTO();
        dto.setId(actividad.getId());
        dto.setNombre(actividad.getNombre());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setFechaEjecucion(actividad.getFechaEjecucion());
        dto.setPrioridad(actividad.getPrioridad());
        dto.setRealizada(actividad.getRealizada());
        dto.setCultivoId(actividad.getCultivoId());
        dto.setUserId(actividad.getUserId());
        return dto;
    }
}
