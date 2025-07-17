package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ParcelaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.DireccionRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrarParcelaService {
    private final ParcelaRepository parcelaRepository;
    private final DireccionRepository direccionRepository;

    public Parcela registrarParcela(RegistrarParcelaCommand command) {
        Double latitud = null;
        Double longitud = null;
        if (command.getDireccion() != null) {
            RegistrarParcelaCommand.DireccionRequest d = command.getDireccion();
            latitud = d.getLat();
            longitud = d.getLng();
        }
        Parcela parcela = Parcela.builder()
                .nombre(command.getNombre())
                .ubicacion(command.getUbicacion())
                .usuarioId(command.getUsuarioId())
                .latitud(latitud)
                .longitud(longitud)
                .descripcion(command.getDescripcion())
                .superficie(command.getSuperficie())
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

    public void eliminarParcela(Integer id) {
        if (!parcelaRepository.existsById(id)) {
            throw new RuntimeException("Parcela no encontrada con ID: " + id);
        }
        parcelaRepository.deleteById(id);
    }
}
