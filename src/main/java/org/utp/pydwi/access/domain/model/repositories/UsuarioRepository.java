package org.utp.pydwi.access.domain.model.repositories;

import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.valueobjects.Email;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(Email email);
    Optional<Usuario> findById(Integer id);
    boolean existsByEmail(Email email);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
