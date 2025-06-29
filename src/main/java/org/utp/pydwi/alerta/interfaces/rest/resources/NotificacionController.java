package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.alerta.application.internal.commandservices.NotificacionCommandService;
import org.utp.pydwi.alerta.application.internal.queryservices.NotificacionQueryService;
import org.utp.pydwi.alerta.domain.model.entities.Notificacion;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionCommandService commandService;
    private final NotificacionQueryService queryService;

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(commandService.save(notificacion));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(queryService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/alerta/{alertaId}")
    public ResponseEntity<List<Notificacion>> listarPorAlerta(@PathVariable Integer alertaId) {
        return ResponseEntity.ok(queryService.findByAlertaId(alertaId));
    }
}
