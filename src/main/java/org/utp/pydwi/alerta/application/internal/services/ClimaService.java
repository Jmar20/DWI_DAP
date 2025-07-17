package org.utp.pydwi.alerta.application.internal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClimaService {
    
    @Value("${openweather.api.key:demo_key}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Coordenadas fijas para Lima, Perú (puedes cambiarlas)
    private static final double DEFAULT_LAT = -12.0464;
    private static final double DEFAULT_LON = -77.0428;
    
    public ClimaData obtenerClimaActual() {
        return obtenerClimaActual(DEFAULT_LAT, DEFAULT_LON);
    }
    
    public ClimaData obtenerClimaActual(double latitud, double longitud) {
        try {
            String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                latitud, longitud, apiKey
            );
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("main")) {
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                double temperatura = ((Number) main.get("temp")).doubleValue();
                double humedad = ((Number) main.get("humidity")).doubleValue();
                
                double lluvia = 0.0;
                if (response.containsKey("rain")) {
                    Map<String, Object> rain = (Map<String, Object>) response.get("rain");
                    if (rain.containsKey("1h")) {
                        lluvia = ((Number) rain.get("1h")).doubleValue();
                    }
                }
                
                String ubicacion = "Lima, Perú"; // Por defecto
                if (response.containsKey("name")) {
                    ubicacion = (String) response.get("name");
                }
                
                return new ClimaData(temperatura, humedad, lluvia, ubicacion);
            }
        } catch (Exception e) {
            log.warn("Error al obtener datos del clima de OpenWeather: {}", e.getMessage());
        }
        
        // Datos simulados en caso de error
        return new ClimaData(22.0, 70.0, 0.0, "Lima, Perú (simulado)");
    }
    
    public static class ClimaData {
        private final double temperatura;
        private final double humedad;
        private final double lluvia;
        private final String ubicacion;
        
        public ClimaData(double temperatura, double humedad, double lluvia, String ubicacion) {
            this.temperatura = temperatura;
            this.humedad = humedad;
            this.lluvia = lluvia;
            this.ubicacion = ubicacion;
        }
        
        // Getters
        public double getTemperatura() { return temperatura; }
        public double getHumedad() { return humedad; }
        public double getLluvia() { return lluvia; }
        public String getUbicacion() { return ubicacion; }
        
        // Métodos de utilidad para alertas
        public boolean esTemperaturaExtrema() {
            return temperatura > 35.0 || temperatura < 5.0;
        }
        
        public boolean esLluviaFuerte() {
            return lluvia > 10.0; // más de 10mm/h
        }
        
        public boolean tieneCondicionesExtremas() {
            return esTemperaturaExtrema() || esLluviaFuerte();
        }
        
        public String getDescripcionClima() {
            if (temperatura > 35.0) return "Temperatura muy alta (" + temperatura + "°C)";
            if (temperatura < 5.0) return "Temperatura muy baja (" + temperatura + "°C)";
            if (lluvia > 10.0) return "Lluvia fuerte (" + lluvia + "mm/h)";
            return "Condiciones normales";
        }
    }
}
