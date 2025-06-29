package org.utp.pydwi.gestionadmin.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.gestionadmin.domain.model.entities.Planta;
import org.utp.pydwi.gestionadmin.domain.model.entities.Etapa;
import org.utp.pydwi.gestionadmin.domain.model.repositories.PlantaAdminRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrarPlantaConEtapasService {
    private final PlantaAdminRepository plantaRepository;

    public Planta registrarPlantaConEtapas(RegistrarPlantaConEtapasCommand command) {
        System.out.println("DEBUG nombreComun recibido: " + command.getNombreComun());
        Planta planta = Planta.builder()
                .nombreComun(command.getNombreComun())
                .nombreCientifico(command.getNombreCientifico())
                .rangoFinal(command.getRangoFinal())
                .build();
        if (command.getEtapas() != null) {
            List<Etapa> etapas = command.getEtapas().stream().map(e ->
                    Etapa.builder()
                            .nombreEtapa(e.getNombreEtapa())
                            .rangotiempo(e.getRangotiempo())
                            .planta(planta)
                            .build()
            ).collect(Collectors.toList());
            planta.setEtapas(etapas);
        }
        return plantaRepository.save(planta);
    }
}
