package org.utp.pydwi.planificacion.interfaces.rest.resources;

import org.utp.pydwi.planificacion.application.internal.commandservices.PlanificacionCommandService;
import org.utp.pydwi.planificacion.application.internal.queryservices.PlanificacionQueryService;
import org.utp.pydwi.planificacion.application.internal.queryservices.CalculoMaduracionService;
import org.utp.pydwi.planificacion.domain.model.entities.Planificacion;
import org.utp.pydwi.planificacion.domain.model.services.PlanificacionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planificaciones")
@RequiredArgsConstructor
public class PlanificacionController {
    private final PlanificacionCommandService commandService;
    private final PlanificacionQueryService queryService;
    private final PlanificacionDomainService domainService;
    private final CalculoMaduracionService calculoMaduracionService;

    @PostMapping
    public ResponseEntity<Planificacion> crear(@RequestBody PlanificacionDTO dto) {
        Planificacion plan = Planificacion.builder()
            .fechaSiembra(dto.getFechaSiembra())
            .fechaCosecha(dto.getFechaCosecha())
            .fechaMaduracionEstimada(dto.getFechaMaduracionEstimada())
            .cultivoId(dto.getCultivoId())
            .build();
        return ResponseEntity.ok(commandService.save(plan));
    }

    @GetMapping
    public ResponseEntity<List<Planificacion>> listar() {
        return ResponseEntity.ok(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planificacion> obtener(@PathVariable Integer id) {
        return queryService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cultivo/{cultivoId}")
    public ResponseEntity<List<Planificacion>> obtenerPorCultivo(@PathVariable Integer cultivoId) {
        return ResponseEntity.ok(queryService.findAllByCultivoId(cultivoId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Planificacion> actualizar(@PathVariable Integer id, @RequestBody PlanificacionDTO dto) {
        return queryService.findById(id)
            .map(plan -> {
                plan.setFechaSiembra(dto.getFechaSiembra());
                plan.setFechaCosecha(dto.getFechaCosecha());
                plan.setFechaMaduracionEstimada(dto.getFechaMaduracionEstimada());
                plan.setCultivoId(dto.getCultivoId());
                return ResponseEntity.ok(commandService.save(plan));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calcular-maduracion")
    public ResponseEntity<CalculoMaduracionResponse> calcularMaduracion(@RequestBody CalculoMaduracionRequest req) {
        LocalDate fechaMaduracion = domainService.calcularFechaMaduracion(req.getFechaSiembra(), req.getDiasMaduracion());
        return ResponseEntity.ok(new CalculoMaduracionResponse(fechaMaduracion));
    }



    @PostMapping("/calcular-rango-maduracion-cultivo")
    public ResponseEntity<CalculoRangoMaduracionResponse> calcularRangoMaduracionPorCultivo(@RequestBody CalculoMaduracionPorCultivoRequest req) {
        LocalDate[] rango = calculoMaduracionService.calcularRangoMaduracionPorCultivo(req.getCultivoId());
        return ResponseEntity.ok(new CalculoRangoMaduracionResponse(rango[0], rango[1]));
    }

    @PostMapping("/cultivo/{cultivoId}/parcela/{parcelaId}")
    public ResponseEntity<Planificacion> crearPorCultivoYParcela(@PathVariable Integer cultivoId, @PathVariable Integer parcelaId, @RequestBody PlanificacionDTO dto) {
        // Asume que el DTO puede traer fechas, pero se asocia a cultivo y parcela dados
        Planificacion plan = Planificacion.builder()
            .fechaSiembra(dto.getFechaSiembra())
            .fechaCosecha(dto.getFechaCosecha())
            .fechaMaduracionEstimada(dto.getFechaMaduracionEstimada())
            .cultivoId(cultivoId)
            .build();
        // Aquí podrías validar que el cultivo y la parcela existen y están relacionados
        return ResponseEntity.ok(commandService.save(plan));
    }

    @DeleteMapping("/cultivo/{cultivoId}/parcela/{parcelaId}")
    public ResponseEntity<Void> eliminarPorCultivoYParcela(@PathVariable Integer cultivoId, @PathVariable Integer parcelaId) {
        // Busca la planificación por cultivo y elimina si existe
        List<Planificacion> planificaciones = queryService.findAllByCultivoId(cultivoId);
        boolean eliminado = false;
        for (Planificacion plan : planificaciones) {
            // Aquí podrías validar que el cultivo pertenece a la parcela
            if (plan.getCultivoId().equals(cultivoId)) {
                commandService.deleteById(plan.getId());
                eliminado = true;
            }
        }
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
