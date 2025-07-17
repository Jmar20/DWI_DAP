package org.utp.pydwi.access.interfaces.rest.resources;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utp.pydwi.access.application.internal.commandservices.AuthenticationService;
import org.utp.pydwi.access.application.internal.commandservices.LoginRequest;
import org.utp.pydwi.access.application.internal.commandservices.RegistroUsuarioCommand;
import org.utp.pydwi.access.infrastructure.security.jwt.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegistroUsuarioCommand command, HttpServletResponse response) {
        String token = authenticationService.register(command);
        
        // Crear cookie HTTP-only más robusta
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // false para desarrollo local (HTTP)
        cookie.setPath("/"); // Disponible en todo el dominio
        cookie.setMaxAge(24 * 60 * 60); // 24 horas
        cookie.setDomain("localhost"); // Especificar dominio explícitamente
        response.addCookie(cookie);
        
        // Headers adicionales para asegurar que las cookies se establezcan
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Set-Cookie", 
            String.format("authToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax", 
                token, 24 * 60 * 60));
        
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "Usuario registrado exitosamente");
        responseData.put("success", "true");
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var loginResult = authenticationService.loginWithUser(request);
        String token = loginResult.getToken();
        var user = loginResult.getUser();
        
        // Crear cookie HTTP-only más robusta
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // false para desarrollo local (HTTP)
        cookie.setPath("/"); // Disponible en todo el dominio
        cookie.setMaxAge(24 * 60 * 60); // 24 horas
        cookie.setDomain("localhost"); // Especificar dominio explícitamente
        response.addCookie(cookie);
        
        // Headers adicionales para asegurar que las cookies se establezcan
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Set-Cookie", 
            String.format("authToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax", 
                token, 24 * 60 * 60));
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "Inicio de sesión exitoso");
        responseData.put("success", true);
        responseData.put("token", token);
        responseData.put("userId", user.getId());
        responseData.put("userName", user.getNombre().getValue());
        responseData.put("userEmail", user.getEmail().getValue());
        responseData.put("userRole", user.getRol().toString());
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String token = null;
        
        // Buscar token en cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        // Si no hay token en cookies, buscar en header como fallback
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }
        
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token no encontrado"));
        }
        
        try {
            String email = jwtService.extractUsername(token);
            if (email != null && !jwtService.isTokenExpired(token)) {
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "email", email,
                    "token", token
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token inválido o expirado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token inválido: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Invalidar cookie del token
        Cookie cookie = new Cookie("authToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Cambiar a true en producción
        cookie.setPath("/");
        cookie.setMaxAge(0); // Eliminar cookie inmediatamente
        response.addCookie(cookie);
        
        return ResponseEntity.ok(Map.of("message", "Logout exitoso", "success", "true"));
    }
}
