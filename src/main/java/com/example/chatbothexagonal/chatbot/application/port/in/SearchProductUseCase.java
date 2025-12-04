package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.ProductResponseDTO;

import java.util.List;

public interface SearchProductUseCase {

    List<ProductResponseDTO> search(String keyword);
}