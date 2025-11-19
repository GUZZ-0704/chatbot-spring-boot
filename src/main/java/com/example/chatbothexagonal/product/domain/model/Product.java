package com.example.chatbothexagonal.product.domain.model;

import com.example.chatbothexagonal.product.domain.exception.InvalidProductPriceException;
import com.example.chatbothexagonal.product.domain.valueobject.CategoryId;
import com.example.chatbothexagonal.product.domain.valueobject.Price;
import com.example.chatbothexagonal.product.domain.valueobject.ProductId;
import com.example.chatbothexagonal.product.domain.valueobject.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private final ProductId id;
    private String name;
    private String description;
    private Price price;
    private Stock stock;
    private ProductStatus status;
    private CategoryId categoryId;
    private final LocalDateTime createdAt;
    private final List<ProductAttribute> attributes;

    public Product(
            ProductId id,
            String name,
            String description,
            Price price,
            Stock stock,
            CategoryId categoryId,
            ProductStatus status,
            LocalDateTime createdAt,
            List<ProductAttribute> attributes
    ) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");

        if (price == null)
            throw new InvalidProductPriceException("El precio es obligatorio");

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.status = status != null ? status : ProductStatus.ACTIVE;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.attributes = attributes != null ? attributes : new ArrayList<>();
    }

    public void updatePrice(Price newPrice) {
        this.price = newPrice;
    }

    public void updateStock(Stock newStock) {
        this.stock = newStock;
        if (this.stock.value() == 0)
            this.status = ProductStatus.OUT_OF_STOCK;
    }

    public void activate() { this.status = ProductStatus.ACTIVE; }
    public void deactivate() { this.status = ProductStatus.INACTIVE; }
    public void discontinue() { this.status = ProductStatus.DISCONTINUED; }

    public ProductId getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Price getPrice() { return price; }
    public Stock getStock() { return stock; }
    public ProductStatus getStatus() { return status; }
    public CategoryId getCategoryId() { return categoryId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<ProductAttribute> getAttributes() { return attributes; }
}