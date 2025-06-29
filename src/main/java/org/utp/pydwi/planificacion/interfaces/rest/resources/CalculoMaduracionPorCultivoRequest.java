package org.utp.pydwi.planificacion.interfaces.rest.resources;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoMaduracionPorCultivoRequest {
    private Integer cultivoId;
}
