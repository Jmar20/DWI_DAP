package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Integer> {
    List<Parcela> findByUsuarioId(Integer usuarioId);
}
