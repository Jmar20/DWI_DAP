package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.alerta.application.internal.commandservices.AlertaCommandService;
import org.utp.pydwi.alerta.application.internal.queryservices.AlertaQueryService;
import org.utp.pydwi.alerta.application.internal.services.AlertaService;
import org.utp.pydwi.alerta.domain.model.entities.Alerta;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alerta")
@RequiredArgsConstructor
public class AlertaController {
    private final AlertaCommandService commandService;
    private final AlertaQueryService queryService;
    private final AlertaService alertaService; // Nuevo servicio de alertas automáticas

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

    @GetMapping("/alertas/usuario/{usuarioId}")
    public ResponseEntity<List<Alerta>> obtenerAlertasPorUsuario(@PathVariable Integer usuarioId) {
        // Por ahora retornamos todas las alertas
        // TODO: Implementar lógica para filtrar por usuario cuando esté disponible en el QueryService
        return ResponseEntity.ok(queryService.findAll());
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

    // NUEVOS ENDPOINTS PARA ALERTAS AUTOMÁTICAS
    @GetMapping("/automaticas/usuario/{userId}")
    public ResponseEntity<List<AlertaDTO>> obtenerAlertasAutomaticas(@PathVariable Integer userId) {
        try {
            List<AlertaDTO> alertas = alertaService.generarAlertasUsuario(userId);
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/automaticas/usuario/{userId}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasAlertasAutomaticas(@PathVariable Integer userId) {
        try {
            List<AlertaDTO> alertas = alertaService.generarAlertasUsuario(userId);

            long criticas = alertas.stream().filter(a -> "CRITICO".equals(a.getNivel())).count();
            long altas = alertas.stream().filter(a -> "ALTO".equals(a.getNivel())).count();
            long medias = alertas.stream().filter(a -> "MEDIO".equals(a.getNivel())).count();
            long bajas = alertas.stream().filter(a -> "BAJO".equals(a.getNivel())).count();

            Map<String, Object> estadisticas = Map.of(
                    "total", alertas.size(),
                    "criticas", criticas,
                    "altas", altas,
                    "medias", medias,
                    "bajas", bajas,
                    "porTipo", alertas.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            AlertaDTO::getTipo,
                            java.util.stream.Collectors.counting()
                    ))
            );

            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
