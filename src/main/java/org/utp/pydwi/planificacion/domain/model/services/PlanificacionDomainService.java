package org.utp.pydwi.planificacion.domain.model.services;

import java.time.LocalDate;

public interface PlanificacionDomainService {
    LocalDate calcularFechaMaduracion(LocalDate fechaSiembra, int diasMaduracion);
}
