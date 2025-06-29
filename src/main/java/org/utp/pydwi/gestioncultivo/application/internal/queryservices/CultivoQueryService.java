package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Etapa;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CultivoQueryService {
    public List<Cultivo> findByParcelaId(Integer parcelaId) {
        // TODO: Implementar consulta real
        return List.of();
    }
    public List<Cultivo> findByUsuarioId(Integer usuarioId) {
        // TODO: Implementar consulta real
        return List.of();
    }
    public List<Actividad> findActividadesByCultivoId(Integer cultivoId) {
        // TODO: Implementar consulta real
        return List.of();
    }
    public List<Etapa> findEtapasByCultivoId(Integer cultivoId) {
        // TODO: Implementar consulta real
        return List.of();
    }
    public Cultivo findDetalleByCultivoId(Integer cultivoId) {
        // TODO: Implementar consulta real
        return null;
    }
    public List<Cultivo> findAll() {
        // TODO: Implementar consulta real a la base de datos
        return List.of();
    }
}
