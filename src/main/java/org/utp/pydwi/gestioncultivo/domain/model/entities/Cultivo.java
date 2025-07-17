package org.utp.pydwi.gestioncultivo.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // ✅ NUEVO CAMPO: Variedad del cultivo
    @Column(name = "variedad")
    private String variedad;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    // ✅ NUEVO CAMPO: Fecha estimada de cosecha
    @Column(name = "fecha_cosecha_estimada")
    private LocalDate fechaCosechaEstimada;

    // ✅ NUEVO CAMPO: Estado del cultivo
    @Column(name = "estado")
    private String estado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parcela")
    @JsonIgnore
    private Parcela parcela;

    @Column(name = "id_parcela", insertable = false, updatable = false)
    private Integer parcelaId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planta")
    @JsonIgnore
    private Planta planta;

    @Column(name = "id_planta", insertable = false, updatable = false)
    private Integer plantaId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // ...existing code...
}
