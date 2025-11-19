package com.example.chatbothexagonal.product.application.usecase;

import com.example.chatbothexagonal.product.application.dto.ProductResponse;
import com.example.chatbothexagonal.product.application.port.in.GetMostExpensiveProductUseCase;
import com.example.chatbothexagonal.product.application.port.out.LoadProductsPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetMostExpensiveProductService implements GetMostExpensiveProductUseCase {

    private final LoadProductsPort loadProductsPort;

    public GetMostExpensiveProductService(LoadProductsPort loadProductsPort) {
        this.loadProductsPort = loadProductsPort;
    }

    @Override
    public ProductResponse get() {
        var product = loadProductsPort.loadAll().stream()
                .max((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .orElseThrow(() -> new RuntimeException("No products found"));

        return new ProductResponse(
                product.getId().value().toString(),
                product.getName(),
                product.getPrice()
        );
    }
}

