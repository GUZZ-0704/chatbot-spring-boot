package com.example.chatbothexagonal.chatbot.application.dto;

public class ChatMessageRequest {
    private final String sessionKey;
    private final String messageText;

    private final String branchId;

    public ChatMessageRequest(String sessionKey, String messageText, String branchId) {
        this.sessionKey = sessionKey;
        this.messageText = messageText;
        this.branchId = branchId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getMessageText() {
        return messageText;
    }
    public String getBranchId() {
        return branchId;
    }
}
