package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaDTO {
    private Integer id;
    private String descripcion;
    private String tipo;
    private LocalDate fecha;
    private Integer actividadId;
}
