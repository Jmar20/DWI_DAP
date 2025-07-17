package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CultivoCreacionRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String variedad;
    
    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;
    
    @NotNull(message = "El usuario ID es obligatorio")
    private Integer userId;  // Cambiado de usuarioId a userId para consistencia
    
    private Integer parcelaId; // Opcional
    private Integer plantaId;  // Opcional
}
