package org.utp.pydwi.access.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Value;

@Embeddable
@Value
public class Nombre {
    String value;

    public Nombre() {
        this.value = null;
    }

    public Nombre(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder los 100 caracteres");
        }
        this.value = value;
    }
}
