package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ParcelaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.DireccionRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrarParcelaService {
    private final ParcelaRepository parcelaRepository;
    private final DireccionRepository direccionRepository;

    public Parcela registrarParcela(RegistrarParcelaCommand command) {
        Parcela parcela = Parcela.builder()
                .nombre(command.getNombre())
                .ubicacion(command.getUbicacion())
                .usuarioId(command.getUsuarioId())
                .build();
        Parcela savedParcela = parcelaRepository.save(parcela);
        if (command.getDireccion() != null) {
            RegistrarParcelaCommand.DireccionRequest d = command.getDireccion();
            Direccion direccion = Direccion.builder()
                    .latitud(d.getLat())
                    .longitud(d.getLng())
                    .descripcion(d.getDescripcion())
                    .parcela(savedParcela)
                    .build();
            direccionRepository.save(direccion);
        }
        return savedParcela;
    }
}
