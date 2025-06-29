package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RegistrarActividadCommand {
    @NotNull
    private String tipo;
    @NotNull
    private LocalDate fecha;
    @NotNull
    private Integer cultivoId;
}
