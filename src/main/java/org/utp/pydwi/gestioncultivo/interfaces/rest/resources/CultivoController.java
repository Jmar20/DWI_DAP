package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarCultivoCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarCultivoService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Etapa;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.CultivoQueryService;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarActividadService;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/gestioncultivo/cultivos")
@RequiredArgsConstructor
public class CultivoController {
    private final RegistrarCultivoService registrarCultivoService;
    private final CultivoQueryService cultivoQueryService;
    private final RegistrarActividadService registrarActividadService;

    @PostMapping
    public ResponseEntity<Cultivo> registrarCultivo(@Valid @RequestBody RegistrarCultivoCommand command) {
        Cultivo cultivo = registrarCultivoService.registrarCultivo(command);
        return ResponseEntity.ok(cultivo);
    }

    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<Cultivo>> listarPorParcela(@PathVariable Integer parcelaId) {
        List<Cultivo> cultivos = cultivoQueryService.findByParcelaId(parcelaId);
        if (cultivos == null || cultivos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cultivos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cultivo>> listarPorUsuario(@PathVariable Integer usuarioId) {
        List<Cultivo> cultivos = cultivoQueryService.findByUsuarioId(usuarioId);
        if (cultivos == null || cultivos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cultivos);
    }

    @PostMapping("/{cultivoId}/actividades")
    public ResponseEntity<Actividad> registrarActividad(@PathVariable Integer cultivoId, @RequestBody RegistrarActividadCommand command) {
        Actividad actividad = registrarActividadService.registrarActividad(cultivoId, command);
        if (actividad == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(actividad);
    }

    @GetMapping("/{cultivoId}/actividades")
    public ResponseEntity<List<Actividad>> listarActividades(@PathVariable Integer cultivoId) {
        List<Actividad> actividades = cultivoQueryService.findActividadesByCultivoId(cultivoId);
        if (actividades == null || actividades.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/{cultivoId}/etapas")
    public ResponseEntity<List<Etapa>> listarEtapas(@PathVariable Integer cultivoId) {
        List<Etapa> etapas = cultivoQueryService.findEtapasByCultivoId(cultivoId);
        if (etapas == null || etapas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etapas);
    }

    @GetMapping("/{cultivoId}/detalle")
    public ResponseEntity<Cultivo> detalleCultivo(@PathVariable Integer cultivoId) {
        Cultivo cultivo = cultivoQueryService.findDetalleByCultivoId(cultivoId);
        if (cultivo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cultivo);
    }
}
