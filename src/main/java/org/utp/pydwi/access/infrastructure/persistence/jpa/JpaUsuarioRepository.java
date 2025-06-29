package org.utp.pydwi.access.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.valueobjects.Email;

import java.util.Optional;

public interface JpaUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(Email email);
    boolean existsByEmail(Email email);
}
