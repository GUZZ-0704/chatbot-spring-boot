package com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto;

import java.util.UUID;

public record TransferRequestDTO(
        UUID productId,
        UUID batchId,
        UUID sourceBranchId,
        UUID destinationBranchId,
        Integer quantity,
        String reason,
        String performedBy
) {}
