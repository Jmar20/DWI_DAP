package org.utp.pydwi.gestioncultivo.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cultivo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cultivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parcela")
    private Parcela parcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planta")
    private Planta planta;

    public Integer getIdPlanta() {
        return planta != null ? planta.getId() : null;
    }
}
