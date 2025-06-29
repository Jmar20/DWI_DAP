package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarParcelaCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarParcelaService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/gestioncultivo/parcelas")
@RequiredArgsConstructor
public class ParcelaController {
    private final RegistrarParcelaService registrarParcelaService;

    @PostMapping
    public ResponseEntity<Parcela> registrarParcela(@Valid @RequestBody RegistrarParcelaCommand command) {
        Parcela parcela = registrarParcelaService.registrarParcela(command);
        return ResponseEntity.ok(parcela);
    }
}
