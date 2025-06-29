package org.utp.pydwi.gestionadmin.application.internal.commandservices;

import lombok.Data;
import java.util.List;

@Data
public class RegistrarPlantaConEtapasCommand {
    private String nombreComun;
    private String nombreCientifico;
    private String rangoFinal;
    private List<EtapaDTO> etapas;

    @Data
    public static class EtapaDTO {
        private String nombreEtapa;
        private String rangotiempo;
    }
}
