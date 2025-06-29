package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtapaRepository extends JpaRepository<Etapa, Integer> {
    List<Etapa> findByPlantaId(Integer plantaId);
}
