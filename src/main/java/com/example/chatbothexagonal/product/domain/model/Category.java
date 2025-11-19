package com.example.chatbothexagonal.product.domain.model;


import com.example.chatbothexagonal.product.domain.valueobject.CategoryId;

public class Category {

    private final CategoryId id;
    private final String name;

    public Category(CategoryId id, String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nombre de categoría inválido");

        this.id = id;
        this.name = name;
    }

    public CategoryId getId() { return id; }
    public String getName() { return name; }
}