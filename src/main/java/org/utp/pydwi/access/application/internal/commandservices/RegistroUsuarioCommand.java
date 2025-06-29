package org.utp.pydwi.access.application.internal.commandservices;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class RegistroUsuarioCommand {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255)
    String password;
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|AGRICULTOR|TECNICO", message = "Rol inválido")
    String rol;
}
