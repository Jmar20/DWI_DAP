package org.utp.pydwi.access.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.access.application.internal.commandservices.UsuarioCommandService;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.interfaces.rest.resources.ActualizarPerfilRequest;
import org.utp.pydwi.access.interfaces.rest.resources.CambiarPasswordRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final UsuarioCommandService usuarioCommandService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsuarios() {
        List<Map<String, Object>> usuarios = jdbcTemplate.queryForList("SELECT * FROM usuario");
        return ResponseEntity.ok(usuarios);
    }

    /**
     * PUT /api/usuarios/{userId} - Actualizar nombre y email del usuario
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @PathVariable Integer userId,
            @Valid @RequestBody ActualizarPerfilRequest request) {
        try {
            Usuario usuario = usuarioCommandService.actualizarPerfil(userId, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Perfil actualizado exitosamente");
            response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre().getValue(),
                "email", usuario.getEmail().getValue(),
                "rol", usuario.getRol().name()
            ));
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * PUT /api/usuarios/{userId}/password - Cambiar contraseña del usuario
     */
    @PutMapping("/{userId}/password")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @PathVariable Integer userId,
            @Valid @RequestBody CambiarPasswordRequest request) {
        try {
            usuarioCommandService.cambiarPassword(userId, request);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Contraseña actualizada exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * DELETE /api/usuarios/{userId} - Eliminar cuenta del usuario
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> eliminarCuenta(@PathVariable Integer userId) {
        try {
            usuarioCommandService.eliminarCuenta(userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cuenta eliminada exitosamente");
            response.put("userId", userId.toString());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
