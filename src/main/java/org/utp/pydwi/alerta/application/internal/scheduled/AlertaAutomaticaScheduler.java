package org.utp.pydwi.alerta.application.internal.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Cultivo;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Parcela;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Actividad;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Planta;
import org.utp.pydwi.gestioncultivo.domain.model.entities.Direccion;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.CultivoQueryService;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ParcelaQueryService;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.DireccionQueryService;
import org.utp.pydwi.gestioncultivo.application.internal.queryservices.ActividadQueryService;
import org.utp.pydwi.alerta.domain.model.entities.Alerta;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.AlertaRepository;
import org.utp.pydwi.alerta.domain.model.entities.Notificacion;
import org.utp.pydwi.alerta.infrastructure.persistence.jpa.NotificacionRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaAutomaticaScheduler {
    private final CultivoQueryService cultivoQueryService;
    private final ParcelaQueryService parcelaQueryService;
    private final ActividadQueryService actividadQueryService;
    private final DireccionQueryService direccionQueryService;
    private final AlertaRepository alertaRepository;
    private final NotificacionRepository notificacionRepository;
    private final OpenWeatherService openWeatherService;

    // Ejecuta cada día a las 6am
    @Scheduled(cron = "0 0 6 * * *")
    public void generarAlertasAutomaticas() {
        generarAlertasAbono();
        generarAlertasRiegoPorClima();
    }

    private void generarAlertasAbono() {
        List<Cultivo> cultivos = cultivoQueryService.findAll();
        for (Cultivo cultivo : cultivos) {
            Actividad ultimaAbono = actividadQueryService.findUltimaActividadPorTipo(cultivo.getId(), "abono");
            LocalDate hoy = LocalDate.now();
            if (ultimaAbono == null || ChronoUnit.DAYS.between(ultimaAbono.getFecha(), hoy) >= 3) {
                Alerta alerta = new Alerta();
                alerta.setDescripcion("Es momento de realizar abono al cultivo " + cultivo.getTipo());
                alerta.setTipo("abono");
                alerta.setFecha(hoy);
                alerta.setActividadId(ultimaAbono != null ? ultimaAbono.getId() : null);
                alertaRepository.save(alerta);
                Notificacion notificacion = new Notificacion();
                notificacion.setMensaje("Alerta de abono generada para el cultivo " + cultivo.getTipo());
                notificacion.setFecha(hoy);
                notificacion.setAlertaId(alerta.getId());
                notificacionRepository.save(notificacion);
            }
        }
    }

    private void generarAlertasRiegoPorClima() {
        List<Parcela> parcelas = parcelaQueryService.findAll();
        for (Parcela parcela : parcelas) {
            Direccion direccion = direccionQueryService.findByParcelaId(parcela.getId());
            if (direccion == null) continue;
            Double lat = direccion.getLatitud();
            Double lon = direccion.getLongitud();
            OpenWeatherData clima = openWeatherService.getClimaActual(lat, lon);
            if (clima != null && (clima.getPrecipitacion() < 1.0 || clima.getTemperatura() > 30.0)) {
                List<Cultivo> cultivos = cultivoQueryService.findByParcelaId(parcela.getId());
                for (Cultivo cultivo : cultivos) {
                    Actividad ultimaRiego = actividadQueryService.findUltimaActividadPorTipo(cultivo.getId(), "riego");
                    LocalDate hoy = LocalDate.now();
                    if (ultimaRiego == null || ChronoUnit.DAYS.between(ultimaRiego.getFecha(), hoy) >= 1) {
                        Alerta alerta = new Alerta();
                        alerta.setDescripcion("Condiciones climáticas adversas: se recomienda riego en la parcela " + parcela.getNombre());
                        alerta.setTipo("riego");
                        alerta.setFecha(hoy);
                        alerta.setActividadId(ultimaRiego != null ? ultimaRiego.getId() : null);
                        alertaRepository.save(alerta);
                        Notificacion notificacion = new Notificacion();
                        notificacion.setMensaje("Alerta de riego generada para la parcela " + parcela.getNombre());
                        notificacion.setFecha(hoy);
                        notificacion.setAlertaId(alerta.getId());
                        notificacionRepository.save(notificacion);
                    }
                }
            }
        }
    }

    // Servicio real para OpenWeather
    @Component
    public static class OpenWeatherService {
        @Value("${openweather.api.key}")
        private String apiKey;
        private final RestTemplate restTemplate = new RestTemplate();

        public OpenWeatherData getClimaActual(Double lat, Double lon) {
            try {
                String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                    lat, lon, apiKey
                );
                var response = restTemplate.getForObject(url, java.util.Map.class);
                double temp = ((Number)((java.util.Map<?,?>)response.get("main")).get("temp")).doubleValue();
                double rain = 0.0;
                if (response.containsKey("rain")) {
                    Object rainObj = ((java.util.Map<?,?>)response.get("rain")).get("1h");
                    if (rainObj != null) rain = ((Number)rainObj).doubleValue();
                }
                return new OpenWeatherData(rain, temp);
            } catch (Exception e) {
                // Si hay error, retorna valores por defecto
                return new OpenWeatherData(0.0, 25.0);
            }
        }
    }

    public static class OpenWeatherData {
        private double precipitacion;
        private double temperatura;
        public OpenWeatherData(double precipitacion, double temperatura) {
            this.precipitacion = precipitacion;
            this.temperatura = temperatura;
        }
        public double getPrecipitacion() { return precipitacion; }
        public double getTemperatura() { return temperatura; }
    }
}
