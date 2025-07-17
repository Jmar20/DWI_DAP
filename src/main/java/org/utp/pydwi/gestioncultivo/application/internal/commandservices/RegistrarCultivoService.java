package org.utp.pydwi.gestioncultivo.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.CultivoGestionCultivoRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ParcelaRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.PlantaGestionCultivoRepository;
import org.utp.pydwi.gestioncultivo.domain.model.repositories.ActividadRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrarCultivoService {
    private final CultivoGestionCultivoRepository cultivoRepository;
    private final ParcelaRepository parcelaRepository;
    private final PlantaGestionCultivoRepository plantaRepository;
    private final ActividadRepository actividadRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Cultivo registrarCultivo(RegistrarCultivoCommand command) {
        Parcela parcela = null;
        Planta planta = null;
        
        // Manejo opcional de parcela
        if (command.getParcelaId() != null) {
            parcela = parcelaRepository.findById(command.getParcelaId())
                    .orElseThrow(() -> new IllegalArgumentException("Parcela no encontrada"));
        }
        
        // Manejo opcional de planta
        if (command.getPlantaId() != null) {
            planta = plantaRepository.findById(command.getPlantaId())
                    .orElseThrow(() -> new IllegalArgumentException("Planta no encontrada"));
        }
        
        Cultivo cultivo = Cultivo.builder()
                .tipo(command.getTipo())
                .variedad(command.getVariedad())  // ✅ NUEVO CAMPO
                .fechaSiembra(command.getFechaSiembra())
                .fechaCosechaEstimada(command.getFechaCosechaEstimada())  // ✅ NUEVO CAMPO
                .estado(command.getEstado())  // ✅ NUEVO CAMPO
                .userId(command.getUserId())  // ¡CRÍTICO! Asignar el userId
                .parcela(parcela)
                .planta(planta)
                .build();
        Cultivo saved = cultivoRepository.save(cultivo);
        entityManager.flush();
        entityManager.refresh(saved);
        return saved;
    }

    public boolean eliminarCultivo(Integer id) {
        try {
            if (cultivoRepository.existsById(id)) {
                cultivoRepository.deleteById(id);
                entityManager.flush(); // Asegurar que se ejecute inmediatamente
                return true;
            }
            return false; // No existe el cultivo
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cultivo: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un cultivo junto con todas sus dependencias (actividades y alertas)
     * @param id ID del cultivo a eliminar
     * @return true si se eliminó correctamente, false si no existe el cultivo
     */
    public boolean eliminarCultivoConDependencias(Integer id) {
        try {
            if (!cultivoRepository.existsById(id)) {
                return false; // No existe el cultivo
            }

            // 1. Obtener todas las actividades del cultivo para eliminar alertas relacionadas
            var actividades = actividadRepository.findByCultivoId(id);
            
            // 2. Eliminar alertas relacionadas con las actividades del cultivo
            for (var actividad : actividades) {
                try {
                    entityManager.createNativeQuery(
                        "DELETE FROM alerta WHERE actividad_id = ?1"
                    ).setParameter(1, actividad.getId()).executeUpdate();
                } catch (Exception e) {
                    // Continuar si no hay alertas para esta actividad
                }
            }
            entityManager.flush();

            // 3. Eliminar alertas automáticas relacionadas directamente con el cultivo
            // (Solo si la columna cultivo_id existe en la base de datos)
            try {
                entityManager.createNativeQuery(
                    "DELETE FROM alerta WHERE cultivo_id = ?1"
                ).setParameter(1, id).executeUpdate();
                entityManager.flush();
            } catch (Exception e) {
                // La columna cultivo_id no existe aún, continuar sin error
                System.out.println("INFO: La columna cultivo_id no existe en la tabla alerta. Ejecute la migración de base de datos.");
            }

            // 4. Eliminar actividades del cultivo
            actividades.forEach(actividad -> {
                actividadRepository.deleteById(actividad.getId());
            });
            entityManager.flush();

            // 5. Finalmente eliminar el cultivo
            cultivoRepository.deleteById(id);
            entityManager.flush();

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cultivo con dependencias: " + e.getMessage(), e);
        }
    }

    public Cultivo actualizarCultivo(ActualizarCultivoCommand command) {
        try {
            // Verificar que el cultivo existe
            Cultivo cultivoExistente = cultivoRepository.findById(command.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cultivo no encontrado"));
            
            // Manejo opcional de parcela
            Parcela parcela = null;
            if (command.getParcelaId() != null) {
                parcela = parcelaRepository.findById(command.getParcelaId())
                        .orElseThrow(() -> new IllegalArgumentException("Parcela no encontrada"));
            }
            
            // Manejo opcional de planta
            Planta planta = null;
            if (command.getPlantaId() != null) {
                planta = plantaRepository.findById(command.getPlantaId())
                        .orElseThrow(() -> new IllegalArgumentException("Planta no encontrada"));
            }
            
            // Actualizar campos del cultivo existente
            cultivoExistente.setTipo(command.getTipo());
            cultivoExistente.setVariedad(command.getVariedad());  // ✅ NUEVO CAMPO
            cultivoExistente.setFechaSiembra(command.getFechaSiembra());
            cultivoExistente.setFechaCosechaEstimada(command.getFechaCosechaEstimada());  // ✅ NUEVO CAMPO
            cultivoExistente.setEstado(command.getEstado());  // ✅ NUEVO CAMPO
            cultivoExistente.setUserId(command.getUserId());
            cultivoExistente.setParcela(parcela);
            cultivoExistente.setPlanta(planta);
            
            // Guardar los cambios
            Cultivo updated = cultivoRepository.save(cultivoExistente);
            entityManager.flush();
            entityManager.refresh(updated);
            return updated;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cultivo: " + e.getMessage(), e);
        }
    }
}
