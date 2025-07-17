package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ActualizarCultivoCommand {
    @NotNull(message = "El ID es obligatorio")
    private Integer id;
    
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;
    
    private String variedad;
    
    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;
    
    private LocalDate fechaCosechaEstimada; // ✅ NUEVO CAMPO
    
    private String estado; // ✅ NUEVO CAMPO
    
    @NotNull(message = "El usuario ID es obligatorio")
    private Integer userId;
    
    private Integer parcelaId;
    private Integer plantaId;
}
