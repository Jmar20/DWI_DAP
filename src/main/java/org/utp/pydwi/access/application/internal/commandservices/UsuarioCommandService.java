package org.utp.pydwi.access.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.repositories.UsuarioRepository;
import org.utp.pydwi.access.domain.model.valueobjects.Email;
import org.utp.pydwi.access.domain.model.valueobjects.Nombre;
import org.utp.pydwi.access.interfaces.rest.resources.ActualizarPerfilRequest;
import org.utp.pydwi.access.interfaces.rest.resources.CambiarPasswordRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioCommandService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario actualizarPerfil(Integer userId, ActualizarPerfilRequest request) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el nuevo email ya está en uso por otro usuario
        Email nuevoEmail = new Email(request.getEmail());
        if (usuarioRepository.existsByEmail(nuevoEmail)) {
            Usuario usuarioConEmail = usuarioRepository.findByEmail(nuevoEmail)
                    .orElse(null);
            if (usuarioConEmail != null && !usuarioConEmail.getId().equals(userId)) {
                throw new RuntimeException("El email ya está en uso por otro usuario");
            }
        }

        // Actualizar datos usando reflexión para acceder a campos privados
        try {
            var nombreField = Usuario.class.getDeclaredField("nombre");
            nombreField.setAccessible(true);
            nombreField.set(usuario, new Nombre(request.getNombre()));

            var emailField = Usuario.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(usuario, nuevoEmail);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage());
        }

        return usuarioRepository.save(usuario);
    }

    public void cambiarPassword(Integer userId, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Verificar que las nuevas contraseñas coincidan
        if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
            throw new RuntimeException("Las nuevas contraseñas no coinciden");
        }

        // Actualizar contraseña
        String passwordEncriptada = passwordEncoder.encode(request.getNuevaPassword());
        usuario.actualizarPassword(passwordEncriptada);
        usuarioRepository.save(usuario);
    }

    public void eliminarCuenta(Integer userId) {
        if (!usuarioRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Aquí podrías agregar lógica adicional para:
        // - Verificar si el usuario tiene datos relacionados que no se pueden eliminar
        // - Anonimizar datos en lugar de eliminar completamente
        // - Registrar la eliminación en logs de auditoría

        usuarioRepository.deleteById(userId);
    }
}
