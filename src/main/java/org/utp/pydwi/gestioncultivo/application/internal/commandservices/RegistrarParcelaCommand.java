package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RegistrarParcelaCommand {
    @NotNull
    private String nombre;
    private String ubicacion; // sitio o referencia textual
    @NotNull
    private Integer usuarioId;
    private DireccionRequest direccion;

    @Data
    public static class DireccionRequest {
        @NotNull
        private Double lat;
        @NotNull
        private Double lng;
        private String descripcion;
    }
}
