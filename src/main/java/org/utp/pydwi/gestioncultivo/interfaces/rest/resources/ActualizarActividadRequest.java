package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ActualizarActividadRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "La fecha de ejecución es obligatoria")
    private LocalDate fechaEjecucion;
    
    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad; // ALTA, MEDIA, BAJA
    
    private Boolean realizada;
    
    @NotNull(message = "El cultivo ID es obligatorio")
    private Integer cultivoId;
    
    @NotNull(message = "El usuario ID es obligatorio")
    private Integer userId;
}
