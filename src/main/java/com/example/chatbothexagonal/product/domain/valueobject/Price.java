package com.example.chatbothexagonal.product.domain.valueobject;

import com.example.chatbothexagonal.product.domain.exception.InvalidProductPriceException;

public record Price(double value) {

    public Price {
        if (value < 0)
            throw new InvalidProductPriceException("El precio no puede ser negativo");
    }

    public Price increase(double amount) {
        return new Price(value + amount);
    }

    public Price decrease(double amount) {
        if (value - amount < 0)
            throw new InvalidProductPriceException("El precio no puede quedar negativo");
        return new Price(value - amount);
    }
}