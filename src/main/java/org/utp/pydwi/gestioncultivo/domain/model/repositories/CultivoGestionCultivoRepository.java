package org.utp.pydwi.gestioncultivo.domain.model.repositories;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CultivoGestionCultivoRepository extends JpaRepository<Cultivo, Integer> {
    List<Cultivo> findByParcelaId(Integer parcelaId);
}
