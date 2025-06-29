package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RegistrarCultivoCommand {
    @NotNull
    private String tipo;
    @NotNull
    private LocalDate fechaSiembra;
    @NotNull
    private Integer parcelaId;
    @NotNull
    private Integer plantaId;
}
