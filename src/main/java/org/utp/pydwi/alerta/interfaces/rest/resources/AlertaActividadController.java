package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.alerta.application.internal.commandservices.AlertaCommandService;
import org.utp.pydwi.alerta.application.internal.queryservices.AlertaQueryService;
import org.utp.pydwi.alerta.domain.model.entities.Alerta;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/alertas/actividad")
@RequiredArgsConstructor
public class AlertaActividadController {
    private final AlertaCommandService commandService;
    private final AlertaQueryService queryService;

    // Crear alerta de riego o abono asociada a una actividad
    @PostMapping
    public ResponseEntity<Alerta> crearAlertaActividad(@RequestBody AlertaDTO dto) {
        Alerta alerta = Alerta.builder()
                .descripcion(dto.getDescripcion())
                .tipo(dto.getTipo())
                .fecha(dto.getFecha())
                .actividadId(dto.getActividadId())
                .build();
        return ResponseEntity.ok(commandService.save(alerta));
    }

    // Listar alertas de riego o abono por actividad
    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Alerta>> listarPorActividad(@PathVariable Integer actividadId) {
        return ResponseEntity.ok(queryService.findByActividadId(actividadId));
    }

    // Listar alertas de riego o abono por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Alerta>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(queryService.findByTipo(tipo));
    }

    // Listar alertas de riego o abono por fechas
    @GetMapping("/fecha")
    public ResponseEntity<List<Alerta>> listarPorFechas(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(queryService.findByFechaBetween(desde, hasta));
    }
}
