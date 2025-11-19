package com.example.chatbothexagonal.product.infraestructure.out.repositories;

import com.example.chatbothexagonal.product.infraestructure.out.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
}
