package org.utp.pydwi.gestionadmin.domain.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utp.pydwi.gestionadmin.domain.model.entities.Etapa;

public interface EtapaAdminRepository extends JpaRepository<Etapa, Integer> {
}
