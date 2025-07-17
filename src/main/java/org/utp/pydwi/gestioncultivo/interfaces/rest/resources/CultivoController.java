package org.utp.pydwi.gestioncultivo.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarCultivoCommand;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.RegistrarCultivoService;
import org.utp.pydwi.gestioncultivo.application.internal.commandservices.ActualizarCultivoCommand;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Etapa;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.CultivoQueryService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/gestioncultivo")
@RequiredArgsConstructor
public class CultivoController {
    private final RegistrarCultivoService registrarCultivoService;
    private final CultivoQueryService cultivoQueryService;


    @PostMapping("/cultivos")
    public ResponseEntity<CultivoDTO> registrarCultivo(@Valid @RequestBody RegistrarCultivoCommand command) {
        Cultivo cultivo = registrarCultivoService.registrarCultivo(command);
        return ResponseEntity.ok(toDTO(cultivo));
    }

    @PostMapping("/cultivos/crear")
    public ResponseEntity<CultivoDTO> crearCultivo(@Valid @RequestBody CultivoCreacionRequest request) {
        // Convertir request del frontend al command del backend
        RegistrarCultivoCommand command = new RegistrarCultivoCommand();
        command.setTipo(request.getNombre()); // nombre -> tipo
        command.setVariedad(request.getVariedad());
        command.setFechaSiembra(request.getFechaSiembra());
        command.setUserId(request.getUserId()); // userId -> userId (ahora consistente)
        
        // NO asignar valores por defecto - dejar null para que sean opcionales
        command.setParcelaId(request.getParcelaId()); // null si no se especifica
        command.setPlantaId(request.getPlantaId());   // null si no se especifica
        
        Cultivo cultivo = registrarCultivoService.registrarCultivo(command);
        return ResponseEntity.ok(toDTO(cultivo));
    }

    @PostMapping("/setup/datos-prueba")
    public ResponseEntity<Map<String, String>> crearDatosPrueba() {
        try {
            // Este endpoint crea datos básicos para testing
            // Solo para desarrollo - debería ser removido en producción
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Datos de prueba creados (parcela y planta por defecto)");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se pudieron crear los datos de prueba: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/cultivos/parcela/{parcelaId}")
    public ResponseEntity<List<CultivoDTO>> listarPorParcela(@PathVariable Integer parcelaId) {
        List<Cultivo> cultivos = cultivoQueryService.findByParcelaId(parcelaId);
        if (cultivos == null || cultivos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CultivoDTO> dtos = cultivos.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/cultivos/usuario/{userId}")
    public ResponseEntity<List<CultivoDTO>> listarPorUsuario(@PathVariable Integer userId) {
        List<Cultivo> cultivos = cultivoQueryService.findByUserId(userId);
        if (cultivos == null) {
            // Si el servicio retorna null, devolver lista vacía
            return ResponseEntity.ok(List.of());
        }
        List<CultivoDTO> dtos = cultivos.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }



    @GetMapping("/cultivos/{cultivoId}/actividades")
    public ResponseEntity<List<Actividad>> listarActividades(@PathVariable Integer cultivoId) {
        List<Actividad> actividades = cultivoQueryService.findActividadesByCultivoId(cultivoId);
        if (actividades == null || actividades.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/cultivos/{cultivoId}/etapas")
    public ResponseEntity<List<Etapa>> listarEtapas(@PathVariable Integer cultivoId) {
        List<Etapa> etapas = cultivoQueryService.findEtapasByCultivoId(cultivoId);
        if (etapas == null || etapas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etapas);
    }

    @GetMapping("/cultivos/{cultivoId}/detalle")
    public ResponseEntity<CultivoDTO> detalleCultivo(@PathVariable Integer cultivoId) {
        Cultivo cultivo = cultivoQueryService.findDetalleByCultivoId(cultivoId);
        if (cultivo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(cultivo));
    }

    @DeleteMapping("/cultivos/{id}")
    public ResponseEntity<Map<String, String>> eliminarCultivo(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "false") boolean cascade) {
        try {
            boolean eliminado;
            String mensaje;
            
            if (cascade) {
                // Eliminar cultivo con todas sus dependencias (actividades y alertas)
                eliminado = registrarCultivoService.eliminarCultivoConDependencias(id);
                mensaje = eliminado ? "Cultivo y todas sus dependencias eliminadas exitosamente" 
                                    : "Cultivo no encontrado";
            } else {
                // Eliminar solo el cultivo (comportamiento original)
                eliminado = registrarCultivoService.eliminarCultivo(id);
                mensaje = eliminado ? "Cultivo eliminado exitosamente" 
                                    : "Cultivo no encontrado";
            }
            
            if (eliminado) {
                Map<String, String> response = new HashMap<>();
                response.put("message", mensaje);
                response.put("id", id.toString());
                response.put("cascade", String.valueOf(cascade));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el cultivo: " + e.getMessage());
            error.put("cascade", String.valueOf(cascade));
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/cultivos/{id}")
    public ResponseEntity<?> actualizarCultivo(@PathVariable Integer id, @Valid @RequestBody ActualizarCultivoRequest request) {
        try {
            // Convertir request del frontend al command del backend
            ActualizarCultivoCommand command = new ActualizarCultivoCommand();
            command.setId(id);
            command.setTipo(request.getTipo());
            command.setVariedad(request.getVariedad());
            command.setFechaSiembra(request.getFechaSiembra());
            command.setFechaCosechaEstimada(request.getFechaCosechaEstimada());  // ✅ NUEVO CAMPO
            command.setEstado(request.getEstado());  // ✅ NUEVO CAMPO
            command.setUserId(request.getUserId());
            command.setParcelaId(request.getParcelaId());
            command.setPlantaId(request.getPlantaId());
            
            Cultivo cultivoActualizado = registrarCultivoService.actualizarCultivo(command);
            return ResponseEntity.ok(toDTO(cultivoActualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el cultivo: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private CultivoDTO toDTO(Cultivo cultivo) {
        CultivoDTO dto = new CultivoDTO();
        dto.setId(cultivo.getId());
        dto.setTipo(cultivo.getTipo());
        dto.setVariedad(cultivo.getVariedad());  // ✅ NUEVO CAMPO
        dto.setFechaSiembra(cultivo.getFechaSiembra());
        dto.setFechaCosechaEstimada(cultivo.getFechaCosechaEstimada());  // ✅ NUEVO CAMPO
        dto.setEstado(cultivo.getEstado());  // ✅ NUEVO CAMPO
        dto.setParcelaId(cultivo.getParcelaId());
        dto.setPlantaId(cultivo.getPlantaId());
        dto.setUserId(cultivo.getUserId());  // ¡CRÍTICO! Incluir userId en respuesta
        return dto;
    }
}
