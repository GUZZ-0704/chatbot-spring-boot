package com.example.chatbothexagonal.chatbot.application.port.out;


import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.BranchStockResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.ProductResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.StockMovementResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.TransferRequestDTO;

import java.util.List;
import java.util.UUID;

public interface InventoryExternalPort {

    List<ProductResponseDTO> searchProducts(String keyword);

    ProductResponseDTO getProductBySku(String sku);

    List<BranchStockResponseDTO> listStockByBranch(UUID branchId);

    List<BranchStockResponseDTO> listStockByProduct(UUID productId);

    StockMovementResponseDTO requestTransfer(TransferRequestDTO dto);
}
