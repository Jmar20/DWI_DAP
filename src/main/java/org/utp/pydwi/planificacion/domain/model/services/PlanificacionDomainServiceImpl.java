package org.utp.pydwi.planificacion.domain.model.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PlanificacionDomainServiceImpl implements PlanificacionDomainService {
    @Override
    public LocalDate calcularFechaMaduracion(LocalDate fechaSiembra, int diasMaduracion) {
        return fechaSiembra.plusDays(diasMaduracion);
    }
}
