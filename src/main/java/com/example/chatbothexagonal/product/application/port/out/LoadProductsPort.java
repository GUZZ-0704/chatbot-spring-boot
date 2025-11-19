package com.example.chatbothexagonal.product.application.port.out;

import com.example.chatbothexagonal.product.domain.model.Product;

import java.util.List;

public interface LoadProductsPort {
    List<Product> loadAll();
}