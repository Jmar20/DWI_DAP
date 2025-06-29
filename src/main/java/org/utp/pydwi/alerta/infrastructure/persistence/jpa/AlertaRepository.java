package org.utp.pydwi.alerta.infrastructure.persistence.jpa;

import org.utp.pydwi.alerta.domain.model.entities.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
    List<Alerta> findByTipo(String tipo);
    List<Alerta> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<Alerta> findByActividadId(Integer actividadId);
}
