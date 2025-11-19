package com.example.chatbothexagonal.product.domain.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String name) {
        super("El producto '" + name + "' ya existe");
    }
}