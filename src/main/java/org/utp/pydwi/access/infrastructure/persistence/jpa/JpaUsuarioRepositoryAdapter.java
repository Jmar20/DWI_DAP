package org.utp.pydwi.access.infrastructure.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.repositories.UsuarioRepository;
import org.utp.pydwi.access.domain.model.valueobjects.Email;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    @Override
    public Usuario save(Usuario usuario) {
        return jpaUsuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(Email email) {
        return jpaUsuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaUsuarioRepository.existsByEmail(email);
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return jpaUsuarioRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaUsuarioRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaUsuarioRepository.existsById(id);
    }
}
