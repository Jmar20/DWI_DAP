package org.utp.pydwi.planificacion.infrastructure.persistence.jpa;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CultivoRepository extends JpaRepository<Cultivo, Integer> {
    Optional<Cultivo> findByIdAndParcelaId(Integer id, Integer parcelaId);
    Optional<Cultivo> findById(Integer id);
    List<Cultivo> findAllByParcela_Id(Integer parcelaId);
}
