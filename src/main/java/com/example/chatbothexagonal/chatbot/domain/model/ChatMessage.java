package com.example.chatbothexagonal.chatbot.domain.model;

import com.example.chatbothexagonal.chatbot.domain.exception.InvalidChatMessageException;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChatMessage {
    private final MessageId id;
    private final SessionId sessionId;
    private final MessageRole role;
    private final String messageText;
    private final String rawJson;
    private final ChatbotModel modelUsed;
    private final LocalDateTime createdAt;

    public ChatMessage(
            MessageId id,
            SessionId sessionId,
            MessageRole role,
            String messageText,
            String rawJson,
            ChatbotModel modelUsed,
            LocalDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.sessionId = Objects.requireNonNull(sessionId);
        this.role = Objects.requireNonNull(role);
        this.messageText = validateMessage(messageText);
        this.rawJson = rawJson;
        this.modelUsed = modelUsed;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    private String validateMessage(String msg) {
        if (msg == null || msg.trim().isEmpty())
            throw new InvalidChatMessageException("El mensaje no puede ser vacío");
        return msg;
    }

    public MessageId getId() {
        return id;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public MessageRole getRole() {
        return role;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getRawJson() {
        return rawJson;
    }

    public ChatbotModel getModelUsed() {
        return modelUsed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

