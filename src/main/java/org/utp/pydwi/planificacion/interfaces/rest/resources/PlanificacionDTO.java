package org.utp.pydwi.planificacion.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanificacionDTO {
    private Integer id;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosecha;
    private LocalDate fechaMaduracionEstimada;
    private Integer cultivoId;
}
