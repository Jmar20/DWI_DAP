package org.utp.pydwi.gestionadmin.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestionadmin.application.internal.commandservices.RegistrarPlantaConEtapasCommand;
import org.utp.pydwi.gestionadmin.application.internal.commandservices.RegistrarPlantaConEtapasService;
import org.utp.pydwi.gestionadmin.domain.model.entities.Planta;

@RestController
@RequestMapping("/api/v1/gestionadmin/plantas")
@RequiredArgsConstructor
public class PlantaAdminController {
    private final RegistrarPlantaConEtapasService registrarPlantaConEtapasService;

    @PostMapping
    public ResponseEntity<Planta> registrarPlantaConEtapas(@RequestBody RegistrarPlantaConEtapasCommand command) {
        Planta planta = registrarPlantaConEtapasService.registrarPlantaConEtapas(command);
        return ResponseEntity.ok(planta);
    }
}
