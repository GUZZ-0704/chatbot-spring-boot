package com.example.chatbothexagonal.product.domain.valueobject;

import com.example.chatbothexagonal.product.domain.exception.InvalidStockException;

public record Stock(int value) {
    public Stock {
        if (value < 0)
            throw new InvalidStockException(value);
    }

    public Stock increment(int amount) {
        if (amount < 0) throw new IllegalArgumentException("El incremento no puede ser negativo");
        return new Stock(value + amount);
    }

    public Stock decrement(int amount) {
        if (amount < 0) throw new IllegalArgumentException("El decremento no puede ser negativo");
        if (value - amount < 0)
            throw new InvalidStockException(value - amount);
        return new Stock(value - amount);
    }
}
