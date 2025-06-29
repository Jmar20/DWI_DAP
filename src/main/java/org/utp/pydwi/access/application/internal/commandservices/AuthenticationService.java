package org.utp.pydwi.access.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.utp.pydwi.access.domain.model.entities.Usuario;
import org.utp.pydwi.access.domain.model.valueobjects.Email;
import org.utp.pydwi.access.domain.model.valueobjects.Nombre;
import org.utp.pydwi.access.domain.model.valueobjects.Rol;
import org.utp.pydwi.access.domain.services.UserDomainService;
import org.utp.pydwi.access.infrastructure.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegistroUsuarioCommand command) {
        var usuario = new Usuario(
                new Nombre(command.getNombre()),
                new Email(command.getEmail()),
                passwordEncoder.encode(command.getPassword()),
                Rol.valueOf(command.getRol())
        );

        userDomainService.guardarUsuario(usuario);
        return jwtService.generateToken(usuario);
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userDomainService.buscarPorEmail(new Email(request.getEmail()));
        return jwtService.generateToken(user);
    }
}
