package org.utp.pydwi.planificacion.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.planificacion.infrastructure.persistence.jpa.CultivoRepository;
import org.utp.pydwi.planificacion.infrastructure.persistence.jpa.PlantaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculoMaduracionService {
    private final CultivoRepository cultivoRepository;
    private final PlantaRepository plantaRepository;

    public LocalDate[] calcularRangoMaduracionPorCultivo(Integer cultivoId) {
        Optional<Cultivo> cultivoOpt = cultivoRepository.findById(cultivoId);
        if (cultivoOpt.isEmpty()) throw new RuntimeException("Cultivo no encontrado");
        Cultivo cultivo = cultivoOpt.get();
        Integer plantaId = cultivo.getIdPlanta();
        Optional<Planta> plantaOpt = plantaRepository.findById(plantaId);
        if (plantaOpt.isEmpty()) throw new RuntimeException("Planta no encontrada");
        Planta planta = plantaOpt.get();
        String rangoFinal = planta.getRangoFinal();
        int[] dias = parseRangoDias(rangoFinal);
        LocalDate fechaInicio = cultivo.getFechaSiembra().plusDays(dias[0]);
        LocalDate fechaFin = cultivo.getFechaSiembra().plusDays(dias[1]);
        return new LocalDate[]{fechaInicio, fechaFin};
    }

    private int[] parseRangoDias(String rangoFinal) {
        // Ejemplo: "70-90" => [70,90], "80" => [80,80]
        if (rangoFinal == null || rangoFinal.isEmpty()) return new int[]{0,0};
        if (rangoFinal.contains("-")) {
            String[] partes = rangoFinal.split("-");
            try {
                int min = Integer.parseInt(partes[0].trim());
                int max = Integer.parseInt(partes[1].trim());
                return new int[]{min, max};
            } catch (Exception e) {
                int val = Integer.parseInt(partes[0].trim());
                return new int[]{val, val};
            }
        }
        int val = Integer.parseInt(rangoFinal.trim());
        return new int[]{val, val};
    }
}
