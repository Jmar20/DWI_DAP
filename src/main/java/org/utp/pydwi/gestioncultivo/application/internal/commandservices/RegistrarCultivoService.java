package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.CultivoGestionCultivoRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ParcelaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.PlantaGestionCultivoRepository;

@Service
@RequiredArgsConstructor
public class RegistrarCultivoService {
    private final CultivoGestionCultivoRepository cultivoRepository;
    private final ParcelaRepository parcelaRepository;
    private final PlantaGestionCultivoRepository plantaRepository;

    public Cultivo registrarCultivo(RegistrarCultivoCommand command) {
        Parcela parcela = parcelaRepository.findById(command.getParcelaId())
                .orElseThrow(() -> new IllegalArgumentException("Parcela no encontrada"));
        Planta planta = plantaRepository.findById(command.getPlantaId())
                .orElseThrow(() -> new IllegalArgumentException("Planta no encontrada"));
        Cultivo cultivo = Cultivo.builder()
                .tipo(command.getTipo())
                .fechaSiembra(command.getFechaSiembra())
                .parcela(parcela)
                .planta(planta)
                .build();
        return cultivoRepository.save(cultivo);
    }
}
