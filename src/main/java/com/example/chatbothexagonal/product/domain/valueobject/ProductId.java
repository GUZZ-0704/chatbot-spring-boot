package com.example.chatbothexagonal.product.domain.valueobject;

import java.util.UUID;

public class ProductId {

    private final UUID value;

    public ProductId(UUID value) {
        this.value = value;
    }

    public UUID value() {
        return value;
    }
}