package com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto;

import java.util.UUID;

public record BranchStockQueryDTO(
        UUID branchId,
        String sku
) {}
