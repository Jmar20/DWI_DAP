package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaDTO {
    private String id;  // ID único para la alerta
    private String tipo;  // RIEGO, ACTIVIDAD_ALTA, ACTIVIDAD_ATRASADA, COSECHA_PROXIMA, CLIMA_EXTREMO
    private String titulo;
    private String descripcion;
    private String nivel;  // CRITICO, ALTO, MEDIO, BAJO
    private LocalDateTime fechaGeneracion;
    private Integer cultivoId;
    private String cultivoNombre;
    private Integer actividadId;
    private String actividadNombre;
    private String ubicacion;
    private Double temperatura;
    private Double lluvia;
    private String accionRecomendada;
    
    // Campo para compatibilidad con el sistema existente
    private LocalDate fecha;
}
