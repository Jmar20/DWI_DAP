package org.utp.pydwi.gestionadmin.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestionadmin.application.internal.commandservices.RegistrarPlantaConEtapasCommand;
import org.utp.pydwi.gestionadmin.application.internal.commandservices.RegistrarPlantaConEtapasService;
import org.utp.pydwi.gestionadmin.domain.model.entities.Planta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gestionadmin/plantas")
@RequiredArgsConstructor
public class PlantaAdminController {
    private final RegistrarPlantaConEtapasService registrarPlantaConEtapasService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public ResponseEntity<Planta> registrarPlantaConEtapas(@RequestBody RegistrarPlantaConEtapasCommand command) {
        Planta planta = registrarPlantaConEtapasService.registrarPlantaConEtapas(command);
        return ResponseEntity.ok(planta);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllPlantas() {
        List<Map<String, Object>> plantas = jdbcTemplate.queryForList("SELECT * FROM planta");
        return ResponseEntity.ok(plantas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlanta(@PathVariable Long id) {
        int rows = jdbcTemplate.update("DELETE FROM planta WHERE id = ?", id);
        if (rows > 0) {
            return ResponseEntity.ok().body("Planta eliminada correctamente");
        } else {
            return ResponseEntity.status(404).body("Planta no encontrada");
        }
    }
}
