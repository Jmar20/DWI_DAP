package org.utp.pydwi.alerta.domain.model.entities;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaOpenWeather {
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private Double temperatura;
    private Double humedad;
    private Double lluvia;
    private String ubicacion;
}
