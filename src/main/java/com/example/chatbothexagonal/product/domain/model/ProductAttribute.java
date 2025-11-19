package com.example.chatbothexagonal.product.domain.model;

public class ProductAttribute {

    private final String name;
    private final Object value;
    private final AttributeType type;

    public ProductAttribute(String name, Object value, AttributeType type) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nombre del atributo inválido");

        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() { return name; }
    public Object getValue() { return value; }
    public AttributeType getType() { return type; }
}
