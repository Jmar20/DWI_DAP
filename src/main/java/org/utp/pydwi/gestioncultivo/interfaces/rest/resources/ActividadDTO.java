package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ActividadDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaEjecucion;
    private String prioridad; // ALTA, MEDIA, BAJA
    private Boolean realizada;
    private Integer cultivoId;
    private Integer userId;
}
