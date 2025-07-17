package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarParcelaCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarParcelaService;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ParcelaQueryService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gestioncultivo/parcelas")
@RequiredArgsConstructor
public class ParcelaController {
    private final RegistrarParcelaService registrarParcelaService;
    private final ParcelaQueryService parcelaQueryService;

    @PostMapping
    public ResponseEntity<Parcela> registrarParcela(@Valid @RequestBody RegistrarParcelaCommand command) {
        Parcela parcela = registrarParcelaService.registrarParcela(command);
        return ResponseEntity.ok(parcela);
    }

    @GetMapping
    public ResponseEntity<List<Parcela>> listarParcelas() {
        return ResponseEntity.ok(parcelaQueryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcela> obtenerParcela(@PathVariable Integer id) {
        return parcelaQueryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Parcela>> obtenerParcelasPorUsuario(@PathVariable Integer userId) {
        List<Parcela> parcelas = parcelaQueryService.findByUsuarioId(userId);
        return ResponseEntity.ok(parcelas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parcela> actualizarParcela(@PathVariable Integer id, @Valid @RequestBody RegistrarParcelaCommand command) {
        try {
            Parcela parcelaActualizada = registrarParcelaService.actualizarParcela(id, command);
            return ResponseEntity.ok(parcelaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarParcela(@PathVariable Integer id) {
        try {
            registrarParcelaService.eliminarParcela(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Parcela eliminada exitosamente");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
