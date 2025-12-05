package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.StockMovementResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.TransferInitiationDTO;

public interface RequestTransferUseCase {

    StockMovementResponseDTO requestTransfer(TransferInitiationDTO dto);
}
