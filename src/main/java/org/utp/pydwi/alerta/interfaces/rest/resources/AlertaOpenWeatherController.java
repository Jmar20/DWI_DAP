package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alertas/openweather")
@RequiredArgsConstructor
public class AlertaOpenWeatherController {
    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    // Simulación de alertas externas
    @GetMapping
    public ResponseEntity<List<AlertaOpenWeatherDTO>> listarAlertasClima(
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud) {
        List<AlertaOpenWeatherDTO> alertas = new ArrayList<>();
        if (latitud != null && longitud != null) {
            try {
                String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                    latitud, longitud, apiKey
                );
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                Map body = response.getBody();
                Map main = body != null ? (Map) body.get("main") : null;
                Double temp = main != null ? ((Number) main.get("temp")).doubleValue() : null;
                Double humedad = main != null ? ((Number) main.get("humidity")).doubleValue() : null;
                Double lluvia = null;
                if (body != null && body.containsKey("rain")) {
                    Map rain = (Map) body.get("rain");
                    if (rain != null && rain.get("1h") != null) {
                        lluvia = ((Number) rain.get("1h")).doubleValue();
                    }
                }
                String descripcion = "";
                if (body != null && body.containsKey("weather")) {
                    Object weatherObj = body.get("weather");
                    if (weatherObj instanceof List weatherList && !weatherList.isEmpty()) {
                        Object firstObj = weatherList.get(0);
                        if (firstObj instanceof Map firstMap) {
                            Object descObj = firstMap.get("description");
                            if (descObj != null) descripcion = descObj.toString();
                        }
                    }
                }
                alertas.add(AlertaOpenWeatherDTO.builder()
                    .tipo("CLIMA")
                    .descripcion(descripcion)
                    .fecha(LocalDateTime.now())
                    .temperatura(temp)
                    .humedad(humedad)
                    .lluvia(lluvia)
                    .ubicacion("lat: " + latitud + ", lon: " + longitud)
                    .build());
            } catch (Exception e) {
                alertas.add(AlertaOpenWeatherDTO.builder()
                    .tipo("ERROR")
                    .descripcion("Error al consultar OpenWeather: " + e.getMessage())
                    .fecha(LocalDateTime.now())
                    .build());
            }
        } else {
            alertas.add(AlertaOpenWeatherDTO.builder()
                .tipo("ERROR")
                .descripcion("Debe enviar latitud y longitud")
                .fecha(LocalDateTime.now())
                .build());
        }
        return ResponseEntity.ok(alertas);
    }
}
