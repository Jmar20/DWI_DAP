package org.utp.pydwi.gestioncultivo.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "actividad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Builder.Default
    @Column(nullable = false)
    private boolean realizada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cultivo_id")
    private Cultivo cultivo;
}
