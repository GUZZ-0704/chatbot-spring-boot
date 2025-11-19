package com.example.chatbothexagonal.product.infraestructure.out.mappers;

import com.example.chatbothexagonal.product.domain.model.Product;
import com.example.chatbothexagonal.product.domain.valueobject.ProductId;
import com.example.chatbothexagonal.product.infraestructure.out.entities.ProductEntity;

public class ProductJpaMapper {

    public static Product toDomain(ProductEntity e) {
        return new Product(
                new ProductId(e.getId()),
                e.getName(),
                e.getPrice()
        );
    }
}