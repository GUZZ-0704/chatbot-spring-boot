package com.example.chatbothexagonal.product.application.usecase;


import com.example.chatbothexagonal.product.application.dto.ProductListResponse;
import com.example.chatbothexagonal.product.application.dto.ProductResponse;
import com.example.chatbothexagonal.product.application.port.in.ListProductsUseCase;
import com.example.chatbothexagonal.product.application.port.out.LoadProductsPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListProductsService implements ListProductsUseCase {

    private final LoadProductsPort loadProductsPort;

    public ListProductsService(LoadProductsPort loadProductsPort) {
        this.loadProductsPort = loadProductsPort;
    }

    @Override
    public ProductListResponse list() {
        var products = loadProductsPort.loadAll();

        var dtoList = products.stream()
                .map(p -> new ProductResponse(
                        p.getId().value().toString(),
                        p.getName(),
                        p.getPrice()
                ))
                .toList();

        return new ProductListResponse(dtoList);
    }
}