package com.example.chatbothexagonal.chatbot.infraestructure.external.branch.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.BranchExternalPort;
import com.example.chatbothexagonal.chatbot.infraestructure.external.branch.dto.BranchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Component
public class BranchExternalAdapter implements BranchExternalPort {

    private final WebClient webClient;

    public BranchExternalAdapter(
            @Value("${external.branch-service.url}") String baseUrl
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public BranchResponseDTO getBranchById(UUID branchId) {
        return webClient.get()
                .uri("/api/branches/{id}", branchId)
                .retrieve()
                .bodyToMono(BranchResponseDTO.class)
                .block();
    }

    @Override
    public List<BranchResponseDTO> listAllBranches() {
        return webClient.get()
                .uri("/api/branches")
                .retrieve()
                .bodyToFlux(BranchResponseDTO.class)
                .collectList()
                .block();
    }
}