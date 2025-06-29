package org.utp.pydwi.access.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.repositories.UsuarioRepository;
import org.utp.pydwi.access.domain.model.valueobjects.Email;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UsuarioRepository usuarioRepository;

    public boolean existeUsuario(Email email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarPorEmail(Email email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario guardarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }
}
