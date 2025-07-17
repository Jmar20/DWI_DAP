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

    public Parcela actualizarParcela(Integer id, RegistrarParcelaCommand command) {
        Parcela parcelaExistente = parcelaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcela no encontrada con ID: " + id));

        // Actualizar campos básicos
        parcelaExistente.setNombre(command.getNombre());
        parcelaExistente.setUbicacion(command.getUbicacion());
        parcelaExistente.setDescripcion(command.getDescripcion());
        parcelaExistente.setSuperficie(command.getSuperficie());

        // Actualizar coordenadas si se proporcionan
        if (command.getDireccion() != null) {
            RegistrarParcelaCommand.DireccionRequest d = command.getDireccion();
            parcelaExistente.setLatitud(d.getLat());
            parcelaExistente.setLongitud(d.getLng());
        }

        // Guardar la parcela actualizada
        Parcela parcelaActualizada = parcelaRepository.save(parcelaExistente);

        // Actualizar o crear dirección si se proporciona
        if (command.getDireccion() != null) {
            RegistrarParcelaCommand.DireccionRequest d = command.getDireccion();
            
            // Buscar dirección existente o crear nueva
            Direccion direccionExistente = direccionRepository.findByParcelaId(id).orElse(null);
            
            if (direccionExistente != null) {
                // Actualizar dirección existente
                direccionExistente.setLatitud(d.getLat());
                direccionExistente.setLongitud(d.getLng());
                direccionExistente.setDescripcion(d.getDescripcion());
                direccionRepository.save(direccionExistente);
            } else {
                // Crear nueva dirección
                Direccion nuevaDireccion = Direccion.builder()
                        .latitud(d.getLat())
                        .longitud(d.getLng())
                        .descripcion(d.getDescripcion())
                        .parcela(parcelaActualizada)
                        .build();
                direccionRepository.save(nuevaDireccion);
            }
        }

        return parcelaActualizada;
    }
}
