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
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {
    private final AlertaCommandService commandService;
    private final AlertaQueryService queryService;

    @PostMapping
    public ResponseEntity<Alerta> crear(@RequestBody AlertaDTO dto) {
        Alerta alerta = Alerta.builder()
                .descripcion(dto.getDescripcion())
                .tipo(dto.getTipo())
                .fecha(dto.getFecha())
                .actividadId(dto.getActividadId())
                .build();
        return ResponseEntity.ok(commandService.save(alerta));
    }

    @GetMapping
    public ResponseEntity<List<Alerta>> listar() {
        return ResponseEntity.ok(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtener(@PathVariable Integer id) {
        return queryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Alerta>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(queryService.findByTipo(tipo));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Alerta>> listarPorFechas(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(queryService.findByFechaBetween(desde, hasta));
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Alerta>> listarPorActividad(@PathVariable Integer actividadId) {
        return ResponseEntity.ok(queryService.findByActividadId(actividadId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
