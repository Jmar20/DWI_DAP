package org.utp.pydwi.alerta.application.internal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.utp.pydwi.alerta.interfaces.rest.resources.AlertaDTO;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.CultivoQueryService;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ConsultarActividadesPorCultivoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaService {
    
    private final CultivoQueryService cultivoQueryService;
    private final ConsultarActividadesPorCultivoService actividadService;
    private final ClimaService climaService;
    
    public List<AlertaDTO> generarAlertasUsuario(Integer userId) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            // Obtener cultivos del usuario
            List<Cultivo> cultivos = cultivoQueryService.findByUserId(userId);
            
            for (Cultivo cultivo : cultivos) {
                // 1. Verificar falta de riego
                alertas.addAll(verificarFaltaRiego(cultivo));
                
                // 2. Verificar actividades con prioridad ALTA sin completar
                alertas.addAll(verificarActividadesAltaPrioridad(cultivo));
                
                // 3. Verificar actividades atrasadas
                alertas.addAll(verificarActividadesAtrasadas(cultivo));
                
                // 4. Verificar cultivos próximos a cosecha
                alertas.addAll(verificarCosechaProxima(cultivo));
            }
            
            // 5. Verificar clima extremo (una vez por usuario)
            alertas.addAll(verificarClimaExtremo(userId));
            
        } catch (Exception e) {
            log.error("Error al generar alertas para usuario {}: {}", userId, e.getMessage(), e);
        }
        
        return alertas;
    }
    
    private List<AlertaDTO> verificarFaltaRiego(Cultivo cultivo) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            List<Actividad> actividades = actividadService.obtenerActividadesPorCultivo(cultivo.getId());
            
            // Buscar la última actividad de riego
            Actividad ultimoRiego = actividades.stream()
                .filter(a -> a.getNombre() != null && 
                           a.getNombre().toLowerCase().contains("riego") && 
                           Boolean.TRUE.equals(a.getRealizada()))
                .max((a1, a2) -> a1.getFechaEjecucion().compareTo(a2.getFechaEjecucion()))
                .orElse(null);
            
            LocalDate hoy = LocalDate.now();
            boolean necesitaRiego = false;
            long diasSinRiego = 0;
            
            if (ultimoRiego == null) {
                // No hay riegos registrados
                necesitaRiego = true;
                diasSinRiego = ChronoUnit.DAYS.between(cultivo.getFechaSiembra(), hoy);
            } else {
                diasSinRiego = ChronoUnit.DAYS.between(ultimoRiego.getFechaEjecucion(), hoy);
                necesitaRiego = diasSinRiego >= 3;
            }
            
            if (necesitaRiego) {
                String nivel = diasSinRiego >= 7 ? "CRITICO" : diasSinRiego >= 5 ? "ALTO" : "MEDIO";
                
                alertas.add(AlertaDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .tipo("RIEGO")
                    .titulo("Falta de Riego")
                    .descripcion(String.format("El cultivo %s lleva %d días sin riego", 
                                              cultivo.getTipo(), diasSinRiego))
                    .nivel(nivel)
                    .fechaGeneracion(LocalDateTime.now())
                    .cultivoId(cultivo.getId())
                    .cultivoNombre(cultivo.getTipo())
                    .accionRecomendada("Programar riego inmediatamente")
                    .build());
            }
            
        } catch (Exception e) {
            log.error("Error verificando riego para cultivo {}: {}", cultivo.getId(), e.getMessage());
        }
        
        return alertas;
    }
    
    private List<AlertaDTO> verificarActividadesAltaPrioridad(Cultivo cultivo) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            List<Actividad> actividades = actividadService.obtenerActividadesPorCultivo(cultivo.getId());
            
            List<Actividad> actividadesAltasPendientes = actividades.stream()
                .filter(a -> "ALTA".equals(a.getPrioridad()) && 
                           !Boolean.TRUE.equals(a.getRealizada()))
                .toList();
            
            for (Actividad actividad : actividadesAltasPendientes) {
                alertas.add(AlertaDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .tipo("ACTIVIDAD_ALTA")
                    .titulo("Actividad de Alta Prioridad Pendiente")
                    .descripcion(String.format("La actividad '%s' tiene prioridad ALTA y está pendiente", 
                                              actividad.getNombre()))
                    .nivel("ALTO")
                    .fechaGeneracion(LocalDateTime.now())
                    .cultivoId(cultivo.getId())
                    .cultivoNombre(cultivo.getTipo())
                    .actividadId(actividad.getId())
                    .actividadNombre(actividad.getNombre())
                    .accionRecomendada("Completar la actividad lo antes posible")
                    .build());
            }
            
        } catch (Exception e) {
            log.error("Error verificando actividades alta prioridad para cultivo {}: {}", cultivo.getId(), e.getMessage());
        }
        
        return alertas;
    }
    
    private List<AlertaDTO> verificarActividadesAtrasadas(Cultivo cultivo) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            List<Actividad> actividades = actividadService.obtenerActividadesPorCultivo(cultivo.getId());
            LocalDate hoy = LocalDate.now();
            
            List<Actividad> actividadesAtrasadas = actividades.stream()
                .filter(a -> !Boolean.TRUE.equals(a.getRealizada()) && 
                           a.getFechaEjecucion().isBefore(hoy))
                .toList();
            
            for (Actividad actividad : actividadesAtrasadas) {
                long diasAtraso = ChronoUnit.DAYS.between(actividad.getFechaEjecucion(), hoy);
                String nivel = diasAtraso >= 7 ? "CRITICO" : diasAtraso >= 3 ? "ALTO" : "MEDIO";
                
                alertas.add(AlertaDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .tipo("ACTIVIDAD_ATRASADA")
                    .titulo("Actividad Atrasada")
                    .descripcion(String.format("La actividad '%s' está atrasada por %d días", 
                                              actividad.getNombre(), diasAtraso))
                    .nivel(nivel)
                    .fechaGeneracion(LocalDateTime.now())
                    .cultivoId(cultivo.getId())
                    .cultivoNombre(cultivo.getTipo())
                    .actividadId(actividad.getId())
                    .actividadNombre(actividad.getNombre())
                    .accionRecomendada("Completar actividad atrasada")
                    .build());
            }
            
        } catch (Exception e) {
            log.error("Error verificando actividades atrasadas para cultivo {}: {}", cultivo.getId(), e.getMessage());
        }
        
        return alertas;
    }
    
    private List<AlertaDTO> verificarCosechaProxima(Cultivo cultivo) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            if (cultivo.getFechaCosechaEstimada() != null) {
                LocalDate hoy = LocalDate.now();
                long diasParaCosecha = ChronoUnit.DAYS.between(hoy, cultivo.getFechaCosechaEstimada());
                
                if (diasParaCosecha <= 5 && diasParaCosecha >= 0) {
                    String nivel = diasParaCosecha <= 1 ? "CRITICO" : diasParaCosecha <= 3 ? "ALTO" : "MEDIO";
                    
                    alertas.add(AlertaDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .tipo("COSECHA_PROXIMA")
                        .titulo("Cosecha Próxima")
                        .descripcion(String.format("El cultivo %s está listo para cosecha en %d días", 
                                                  cultivo.getTipo(), diasParaCosecha))
                        .nivel(nivel)
                        .fechaGeneracion(LocalDateTime.now())
                        .cultivoId(cultivo.getId())
                        .cultivoNombre(cultivo.getTipo())
                        .accionRecomendada(diasParaCosecha <= 1 ? "¡Cosechar ahora!" : "Preparar herramientas de cosecha")
                        .build());
                }
            }
            
        } catch (Exception e) {
            log.error("Error verificando cosecha próxima para cultivo {}: {}", cultivo.getId(), e.getMessage());
        }
        
        return alertas;
    }
    
    private List<AlertaDTO> verificarClimaExtremo(Integer userId) {
        List<AlertaDTO> alertas = new ArrayList<>();
        
        try {
            ClimaService.ClimaData clima = climaService.obtenerClimaActual();
            
            if (clima.tieneCondicionesExtremas()) {
                String nivel = "ALTO";
                String accion = "Proteger cultivos de condiciones extremas";
                
                if (clima.getTemperatura() > 35.0) {
                    accion = "Aumentar riego y proporcionar sombra";
                } else if (clima.getTemperatura() < 5.0) {
                    accion = "Proteger del frío con mantas o invernaderos";
                    nivel = "CRITICO";
                } else if (clima.esLluviaFuerte()) {
                    accion = "Verificar drenaje y proteger de encharcamientos";
                }
                
                alertas.add(AlertaDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .tipo("CLIMA_EXTREMO")
                    .titulo("Condiciones Climáticas Extremas")
                    .descripcion(clima.getDescripcionClima())
                    .nivel(nivel)
                    .fechaGeneracion(LocalDateTime.now())
                    .ubicacion(clima.getUbicacion())
                    .temperatura(clima.getTemperatura())
                    .lluvia(clima.getLluvia())
                    .accionRecomendada(accion)
                    .build());
            }
            
        } catch (Exception e) {
            log.error("Error verificando clima extremo: {}", e.getMessage());
        }
        
        return alertas;
    }
}
