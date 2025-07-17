package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CultivoDTO {
    private Integer id;
    private String tipo;
    private String variedad;  // ✅ NUEVO CAMPO
    private LocalDate fechaSiembra;
    private LocalDate fechaCosechaEstimada;  // ✅ NUEVO CAMPO
    private String estado;  // ✅ NUEVO CAMPO
    private Integer parcelaId;
    private Integer plantaId;
    private Integer userId;  // ¡IMPORTANTE! Incluir userId en la respuesta
}
