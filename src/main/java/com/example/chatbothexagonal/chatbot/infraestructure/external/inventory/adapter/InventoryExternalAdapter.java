package com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.InventoryExternalPort;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.BranchStockResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.ProductResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.StockMovementResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.TransferRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryExternalAdapter implements InventoryExternalPort {

    private final WebClient webClient;

    public InventoryExternalAdapter(
            @Value("${external.branch-service.url}") String baseUrl
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String keyword) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/search")
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .bodyToFlux(ProductResponseDTO.class)
                .collectList()
                .block();
    }

    @Override
    public ProductResponseDTO getProductBySku(String sku) {
        return webClient.get()
                .uri("/api/products/sku/{sku}", sku)
                .retrieve()
                .bodyToMono(ProductResponseDTO.class)
                .block();
    }

    @Override
    public List<BranchStockResponseDTO> listStockByBranch(UUID branchId) {
        return webClient.get()
                .uri("/api/branch-stock/branch/{branchId}", branchId)
                .retrieve()
                .bodyToFlux(BranchStockResponseDTO.class)
                .collectList()
                .block();
    }

    @Override
    public List<BranchStockResponseDTO> listStockByProduct(UUID productId) {
        return webClient.get()
                .uri("/api/branch-stock/product/{productId}", productId)
                .retrieve()
                .bodyToFlux(BranchStockResponseDTO.class)
                .collectList()
                .block();
    }

    @Override
    public StockMovementResponseDTO requestTransfer(TransferRequestDTO dto) {
        return webClient.post()
                .uri("/api/movements/transfer")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(StockMovementResponseDTO.class)
                .block();
    }
}
