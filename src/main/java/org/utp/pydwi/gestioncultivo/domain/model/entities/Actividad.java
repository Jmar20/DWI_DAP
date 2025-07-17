package org.utp.pydwi.gestioncultivo.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "tipo", nullable = false)
    private String nombre;

    // Campo adicional para la columna nombre en la BD
    @Column(name = "nombre", nullable = false)
    private String nombreActividad;

    @Column(nullable = true)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fechaEjecucion;

    @Column(nullable = true)
    private String prioridad; // ALTA, MEDIA, BAJA

    @Builder.Default
    @Column(nullable = false)
    private Boolean realizada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cultivo")
    @JsonIgnore
    private Cultivo cultivo;

    @Column(name = "id_cultivo", insertable = false, updatable = false)
    private Integer cultivoId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
