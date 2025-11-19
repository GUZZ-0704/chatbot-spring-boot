package com.example.chatbothexagonal.chatbot.application.dto;

public class ChatMessageRequest {
    private final String sessionKey;
    private final String messageText;

    public ChatMessageRequest(String sessionKey, String messageText) {
        this.sessionKey = sessionKey;
        this.messageText = messageText;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getMessageText() {
        return messageText;
    }
}
