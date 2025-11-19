package com.example.chatbothexagonal.chatbot.application.dto;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;

public class ChangeModelRequest {
    private final String sessionKey;
    private final ChatbotModel newModel;

    public ChangeModelRequest(String sessionKey, ChatbotModel newModel) {
        this.sessionKey = sessionKey;
        this.newModel = newModel;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public ChatbotModel getNewModel() {
        return newModel;
    }
}
