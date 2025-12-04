package com.example.chatbothexagonal.chatbot.domain.model;

import com.example.chatbothexagonal.chatbot.domain.exception.InvalidSessionException;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChatSession {

    private final SessionId id;
    private final Long userId;
    private final String sessionKey;
    private ChatbotModel activeModel;
    private final LocalDateTime createdAt;

    public ChatSession(SessionId id,
                       Long userId,
                       String sessionKey,
                       ChatbotModel activeModel,
                       LocalDateTime createdAt) {

        this.id = Objects.requireNonNull(id);
        this.userId = userId;
        this.sessionKey = validateSessionKey(sessionKey);
        this.activeModel = activeModel == null ? ChatbotModel.UNKNOWN : activeModel;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    private String validateSessionKey(String key) {
        if (key == null || key.trim().isEmpty())
            throw new InvalidSessionException("sessionKey no puede estar vacío");
        return key;
    }

    public void changeModel(ChatbotModel newModel) {
        if (newModel == null)
            throw new InvalidSessionException("Modelo no puede ser null");
        this.activeModel = newModel;
    }

    public SessionId getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public ChatbotModel getActiveModel() {
        return activeModel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
