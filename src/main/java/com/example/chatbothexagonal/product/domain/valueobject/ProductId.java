package com.example.chatbothexagonal.product.domain.valueobject;

import java.util.UUID;

public record ProductId(UUID value) {
    public ProductId {
        if (value == null) throw new IllegalArgumentException("ProductId no puede ser nulo");
    }
}
