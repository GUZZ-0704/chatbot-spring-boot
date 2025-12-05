package com.example.chatbothexagonal.chatbot.infraestructure.in.dto;

import com.example.chatbothexagonal.common.validation.MinWords;
import com.example.chatbothexagonal.common.validation.NoWhitespace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChatMessageRequestDTO {

    @NotBlank(message = "sessionKey es obligatorio")
    @NoWhitespace
    @Size(max = 150, message = "sessionKey demasiado largo")
    private String sessionKey;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 4000, message = "Máximo 4000 caracteres permitidos")
    @MinWords(1)
    private String messageText;

    @NotBlank
    private String branchId;

    public String getSessionKey() { return sessionKey; }
    public String getMessageText() { return messageText; }
    public String getBranchId() { return branchId; }
}
