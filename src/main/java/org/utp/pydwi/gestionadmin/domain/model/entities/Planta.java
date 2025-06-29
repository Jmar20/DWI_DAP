package org.utp.pydwi.gestionadmin.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity(name = "PlantaAdmin")
@Table(name = "planta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombreComun", nullable = false)
    private String nombreComun;

    @Column(name = "nombreCientifico")
    private String nombreCientifico;

    @Column(name = "rango_final")
    private String rangoFinal;

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL)
    private List<Etapa> etapas;
}
