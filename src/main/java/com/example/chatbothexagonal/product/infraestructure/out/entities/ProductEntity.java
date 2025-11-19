package com.example.chatbothexagonal.product.infraestructure.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    public ProductEntity() { }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public double getPrice() { return price; }
}