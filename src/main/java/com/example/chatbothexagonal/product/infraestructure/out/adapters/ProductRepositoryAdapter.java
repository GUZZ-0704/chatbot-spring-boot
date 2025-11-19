package com.example.chatbothexagonal.product.infraestructure.out.adapters;

import com.example.chatbothexagonal.product.application.port.out.LoadProductsPort;
import com.example.chatbothexagonal.product.domain.model.Product;
import com.example.chatbothexagonal.product.infraestructure.out.mappers.ProductJpaMapper;
import com.example.chatbothexagonal.product.infraestructure.out.repositories.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductRepositoryAdapter implements LoadProductsPort {

    private final ProductJpaRepository repository;

    public ProductRepositoryAdapter(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> loadAll() {
        return repository.findAll().stream()
                .map(ProductJpaMapper::toDomain)
                .toList();
    }
}