package org.utp.pydwi.planificacion.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoRangoMaduracionResponse {
    private LocalDate fechaMaduracionInicio;
    private LocalDate fechaMaduracionFin;
}
