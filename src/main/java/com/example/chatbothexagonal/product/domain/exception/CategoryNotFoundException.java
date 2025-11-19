package com.example.chatbothexagonal.product.domain.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super("Categoría no encontrada con id: " + id);
    }
}
