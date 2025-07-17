package org.utp.pydwi.gestioncultivo.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parcela")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;

    // Nuevos campos para geolocalización
    private Double latitud;
    private Double longitud;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    // Campos adicionales para información detallada
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "superficie")
    private Double superficie;
}
