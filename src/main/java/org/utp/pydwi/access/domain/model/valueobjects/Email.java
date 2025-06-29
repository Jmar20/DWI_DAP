package org.utp.pydwi.access.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Value;

@Embeddable
@Value
public class Email {
    String value;

    public Email() {
        this.value = null;
    }

    public Email(String value) {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.value = value;
    }
}
