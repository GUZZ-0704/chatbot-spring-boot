package com.example.chatbothexagonal.chatbot.infraestructure.in.dto;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;
import com.example.chatbothexagonal.common.validation.NoWhitespace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChangeModelRequestDTO {

    @NotBlank(message = "sessionKey es obligatorio")
    @NoWhitespace
    private String sessionKey;

    @NotNull(message = "newModel no puede ser nulo")
    private ChatbotModel newModel;

    public String getSessionKey() { return sessionKey; }
    public ChatbotModel getNewModel() { return newModel; }
}
