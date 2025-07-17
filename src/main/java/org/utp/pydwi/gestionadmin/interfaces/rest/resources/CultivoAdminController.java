package org.utp.pydwi.gestionadmin.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.interfaces.rest.resources.CultivoDTO;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.CultivoGestionCultivoRepository;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gestionadmin/cultivos")
@RequiredArgsConstructor
public class CultivoAdminController {
    private final CultivoGestionCultivoRepository cultivoRepository;

    @GetMapping
    public ResponseEntity<List<CultivoDTO>> getAllCultivos() {
        List<Cultivo> cultivos = cultivoRepository.findAll();
        List<CultivoDTO> dtos = cultivos.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    private CultivoDTO toDTO(Cultivo cultivo) {
        CultivoDTO dto = new CultivoDTO();
        dto.setId(cultivo.getId());
        dto.setTipo(cultivo.getTipo());
        dto.setFechaSiembra(cultivo.getFechaSiembra());
        dto.setParcelaId(cultivo.getParcelaId());
        dto.setPlantaId(cultivo.getPlantaId());
        return dto;
    }
}
