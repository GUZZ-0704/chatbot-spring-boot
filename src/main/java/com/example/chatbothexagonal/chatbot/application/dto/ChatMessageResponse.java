package com.example.chatbothexagonal.chatbot.application.dto;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private final String responseText;
    private final ChatbotModel modelUsed;
    private final LocalDateTime createdAt;

    public ChatMessageResponse(String responseText, ChatbotModel modelUsed, LocalDateTime createdAt) {
        this.responseText = responseText;
        this.modelUsed = modelUsed;
        this.createdAt = createdAt;
    }

    public String getResponseText() {
        return responseText;
    }

    public ChatbotModel getModelUsed() {
        return modelUsed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
