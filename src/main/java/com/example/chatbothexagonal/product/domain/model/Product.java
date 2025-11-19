package com.example.chatbothexagonal.product.domain.model;

import com.example.chatbothexagonal.product.domain.valueobject.ProductId;

public class Product {

    private final ProductId id;
    private final String name;
    private final double price;

    public Product(ProductId id, String name, double price) {
        this.id    = id;
        this.name  = name;
        this.price = price;
    }

    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}