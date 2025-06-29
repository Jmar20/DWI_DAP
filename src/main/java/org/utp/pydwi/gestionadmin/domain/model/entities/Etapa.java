package org.utp.pydwi.gestionadmin.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EtapaAdmin")
@Table(name = "etapa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Etapa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombreEtapa;

    @Column(name = "rango_tiempo")
    private String rangotiempo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planta")
    private Planta planta;
}
