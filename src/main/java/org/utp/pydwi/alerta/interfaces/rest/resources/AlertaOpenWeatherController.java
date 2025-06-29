package org.utp.pydwi.alerta.interfaces.rest.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alertas/openweather")
@RequiredArgsConstructor
public class AlertaOpenWeatherController {
    // Simulación de alertas externas
    @GetMapping
    public ResponseEntity<List<AlertaOpenWeatherDTO>> listarAlertasClima(@RequestParam(required = false) String ubicacion) {
        // Aquí deberías consumir la API real de OpenWeather, esto es solo un mock
        List<AlertaOpenWeatherDTO> alertas = new ArrayList<>();
        alertas.add(AlertaOpenWeatherDTO.builder()
            .tipo("LLUVIA")
            .descripcion("Lluvias intensas en la zona")
            .fecha(LocalDateTime.now())
            .temperatura(18.5)
            .humedad(90.0)
            .lluvia(25.0)
            .ubicacion(ubicacion != null ? ubicacion : "Desconocida")
            .build());
        return ResponseEntity.ok(alertas);
    }
}
