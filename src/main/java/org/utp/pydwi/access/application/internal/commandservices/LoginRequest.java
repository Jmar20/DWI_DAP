package org.utp.pydwi.access.application.internal.commandservices;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class LoginRequest {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    String email;
    @NotBlank(message = "La password es obligatoria")
    String password;
}
