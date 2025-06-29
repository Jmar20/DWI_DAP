package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaOpenWeatherDTO {
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private Double temperatura;
    private Double humedad;
    private Double lluvia;
    private String ubicacion;
}
