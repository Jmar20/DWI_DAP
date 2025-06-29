package org.utp.pydwi.alerta.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "alerta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    private String tipo;

    private LocalDate fecha;

    @Column(name = "actividad_id")
    private Integer actividadId;
}
