package org.utp.pydwi.gestioncultivo.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Etapa;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.CultivoGestionCultivoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // ¡IMPORTANTE! Para inyección de dependencias
public class CultivoQueryService {
    private final CultivoGestionCultivoRepository cultivoRepository;
    
    public List<Cultivo> findByParcelaId(Integer parcelaId) {
        return cultivoRepository.findByParcelaId(parcelaId);
    }
    
    public List<Cultivo> findByUserId(Integer userId) {
        // ¡CRÍTICO! Implementación real de la consulta por userId
        return cultivoRepository.findByUserId(userId);
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
