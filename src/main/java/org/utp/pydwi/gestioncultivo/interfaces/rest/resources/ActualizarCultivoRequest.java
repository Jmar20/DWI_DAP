package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ActualizarCultivoRequest {
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;
    
    private String variedad; // Campo opcional
    
    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;
    
    private LocalDate fechaCosechaEstimada; // ✅ NUEVO CAMPO
    
    private String estado; // ✅ NUEVO CAMPO
    
    @NotNull(message = "El usuario ID es obligatorio")
    private Integer userId;
    
    private Integer parcelaId; // Opcional
    private Integer plantaId;  // Opcional
}
