package org.utp.pydwi.access.application.internal.commandservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.utp.pydwi.access.domain.model.entities.Usuario;

@Data
@AllArgsConstructor
public class LoginResult {
    private String token;
    private Usuario user;
}
