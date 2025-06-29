package org.utp.pydwi.alerta.domain.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String mensaje;

    private java.time.LocalDateTime fecha;
    private Integer usuarioId;
    private Integer alertaId;

    public void setFecha(java.time.LocalDate fecha) {
        this.fecha = fecha.atStartOfDay();
    }
    public void setAlertaId(Integer alertaId) {
        this.alertaId = alertaId;
    }
}
