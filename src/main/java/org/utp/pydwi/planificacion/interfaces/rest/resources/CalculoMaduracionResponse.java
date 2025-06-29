package org.utp.pydwi.planificacion.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoMaduracionResponse {
    private LocalDate fechaMaduracionEstimada;
}
