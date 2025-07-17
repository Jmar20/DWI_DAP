package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClimaResponseDTO {
    private Double temperatura;
    private Double humedad;
    private Double lluvia;
    private String ubicacion;
    private String descripcion;
    private Boolean tieneCondicionesExtremas;
    private Boolean esTemperaturaExtrema;
    private Boolean esLluviaFuerte;
    private LocalDateTime fecha;
}
