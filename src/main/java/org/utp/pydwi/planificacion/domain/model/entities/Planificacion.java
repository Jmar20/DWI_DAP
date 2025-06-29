package org.utp.pydwi.planificacion.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "planificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate fechaSiembra;

    private LocalDate fechaCosecha;

    private LocalDate fechaMaduracionEstimada;

    @Column(nullable = false)
    private Integer cultivoId;
}
