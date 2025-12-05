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

    private ChatPendingAction pendingAction;
    private String pendingProductId;
    private String pendingFromBranchId;

    public ChatSession(
            SessionId id,
            Long userId,
            String sessionKey,
            ChatbotModel activeModel,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.activeModel = activeModel == null ? ChatbotModel.UNKNOWN : activeModel;
        this.createdAt = Objects.requireNonNull(createdAt);

        this.pendingAction = ChatPendingAction.NONE;
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

    public ChatPendingAction getPendingAction() { return pendingAction; }
    public String getPendingProductId() { return pendingProductId; }
    public String getPendingFromBranchId() { return pendingFromBranchId; }

    public void setPendingAction(ChatPendingAction action) {
        this.pendingAction = action;
    }

    public void setPendingProductId(String id) {
        this.pendingProductId = id;
    }

    public void setPendingFromBranchId(String id) {
        this.pendingFromBranchId = id;
    }

    public void clearPending() {
        this.pendingAction = ChatPendingAction.NONE;
        this.pendingProductId = null;
        this.pendingFromBranchId = null;
    }

}
