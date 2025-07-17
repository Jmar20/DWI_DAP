package org.utp.pydwi.gestionadmin.interfaces.rest.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gestionadmin/usuarios")
public class UsuarioAdminController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsuarios() {
        List<Map<String, Object>> usuarios = jdbcTemplate.queryForList("SELECT * FROM usuario");
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        int rows = jdbcTemplate.update("DELETE FROM usuario WHERE id = ?", id);
        if (rows > 0) {
            return ResponseEntity.ok().body("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
