package com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto;

import java.util.UUID;

public record ProductSearchRequestDTO(
        String nameOrSku,
        UUID branchId
) {}
