package com.example.chatbothexagonal.product.domain.valueobject;

import java.util.UUID;

public record CategoryId(UUID value) {
    public CategoryId {
        if (value == null) throw new IllegalArgumentException("CategoryId no puede ser nulo");
    }
}
