package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ParcelaRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParcelaQueryService {
    private final ParcelaRepository parcelaRepository;

    public List<Parcela> findAll() {
        return parcelaRepository.findAll();
    }

    public Optional<Parcela> findById(Integer id) {
        return parcelaRepository.findById(id);
    }

    public List<Parcela> findByUsuarioId(Integer usuarioId) {
        return parcelaRepository.findByUsuarioId(usuarioId);
    }
}
