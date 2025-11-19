package com.example.chatbothexagonal.product.domain.exception;

public class InvalidStockException extends RuntimeException {
    public InvalidStockException(int stock) {
        super("Stock inválido: " + stock);
    }
}
