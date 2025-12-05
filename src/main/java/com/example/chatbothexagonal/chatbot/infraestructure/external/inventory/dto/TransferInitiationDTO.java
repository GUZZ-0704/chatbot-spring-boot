package com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto;

public record TransferInitiationDTO(
        String productId,
        String fromBranchId,
        String toBranchId,
        Integer quantity,
        String customerCode
) {}
