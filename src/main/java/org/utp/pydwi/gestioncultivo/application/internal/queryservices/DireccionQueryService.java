package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.DireccionRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DireccionQueryService {
    private final DireccionRepository direccionRepository;

    public Direccion findByParcelaId(Integer parcelaId) {
        return direccionRepository.findByParcelaId(parcelaId).orElse(null);
    }
}
