package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadService;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ConsultarActividadesPorCultivoService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gestioncultivo/actividades")
@RequiredArgsConstructor
public class ActividadController {
    private final RegistrarActividadService registrarActividadService;
    private final ConsultarActividadesPorCultivoService consultarActividadesPorCultivoService;

    @PostMapping
    public ResponseEntity<Actividad> registrarActividad(@Valid @RequestBody RegistrarActividadCommand command) {
        Actividad actividad = registrarActividadService.registrarActividad(command);
        return ResponseEntity.ok(actividad);
    }

    @GetMapping("/cultivo/{cultivoId}")
    public ResponseEntity<List<Actividad>> obtenerActividadesPorCultivo(@PathVariable Integer cultivoId) {
        List<Actividad> actividades = consultarActividadesPorCultivoService.obtenerActividadesPorCultivo(cultivoId);
        return ResponseEntity.ok(actividades);
    }

    @PutMapping("/{actividadId}/realizada")
    public ResponseEntity<Actividad> marcarActividadComoRealizada(@PathVariable Integer actividadId) {
        Actividad actividad = registrarActividadService.marcarComoRealizada(actividadId);
        if (actividad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actividad);
    }
}
