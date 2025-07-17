package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class RegistrarCultivoCommand {
    @NotBlank(message = "El nombre/tipo es obligatorio")
    private String tipo; // Acepta tanto "tipo" como "nombre" del frontend
    
    private String variedad; // Campo opcional para variedad
    
    @NotNull(message = "La fecha de siembra es obligatoria")
    private LocalDate fechaSiembra;
    
    private LocalDate fechaCosechaEstimada; // ✅ NUEVO CAMPO
    
    private String estado; // ✅ NUEVO CAMPO
    
    @NotNull(message = "El usuario ID es obligatorio")
    private Integer userId; // Cambiado de usuarioId a userId para consistencia
    
    private Integer parcelaId; // Campo opcional - puede ser asignado por defecto
    private Integer plantaId;  // Campo opcional - puede ser asignado por defecto
}
