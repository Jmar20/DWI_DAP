package org.utp.pydwi.access.application.internal.queryservices;

import lombok.Value;

@Value
public class UsuarioDTO {
    Long id;
    String nombre;
    String email;
    String rol;
}
