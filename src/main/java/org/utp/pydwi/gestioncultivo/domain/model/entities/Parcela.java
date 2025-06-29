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

    @Column(name = "usuario_id")
    private Integer usuarioId;
}
