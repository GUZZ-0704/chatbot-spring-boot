package com.example.chatbothexagonal.product.application.port.in;

import com.example.chatbothexagonal.product.application.dto.ProductListResponse;

public interface ListProductsUseCase {
    ProductListResponse list();
}