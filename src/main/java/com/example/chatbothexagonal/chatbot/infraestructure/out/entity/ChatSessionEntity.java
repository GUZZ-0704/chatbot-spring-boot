package com.example.chatbothexagonal.chatbot.infraestructure.out.entity;

import com.example.chatbothexagonal.chatbot.domain.model.ChatPendingAction;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
public class ChatSessionEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    private Long userId;

    private String sessionKey;

    @Enumerated(EnumType.STRING)
    private ChatbotModel activeModel;

    private LocalDateTime createdAt;

    private ChatPendingAction pendingAction;
    private String pendingProductId;
    private String pendingFromBranchId;

    public ChatSessionEntity() {
    }

    public ChatSessionEntity(
            UUID id,
            Long userId,
            String sessionKey,
            ChatbotModel activeModel,
            LocalDateTime createdAt,
            ChatPendingAction pendingAction,
            String pendingProductId,
            String pendingFromBranchId
    ) {
        this.id = id;
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.activeModel = activeModel;
        this.createdAt = createdAt;
        this.pendingAction = pendingAction;
        this.pendingProductId = pendingProductId;
        this.pendingFromBranchId = pendingFromBranchId;
    }

    public UUID getId() { return id; }
    public Long getUserId() { return userId; }
    public String getSessionKey() { return sessionKey; }
    public ChatbotModel getActiveModel() { return activeModel; }

    public ChatPendingAction getPendingAction() { return pendingAction; }
    public void setPendingAction(ChatPendingAction pendingAction) { this.pendingAction = pendingAction; }

    public void setPendingProductId(String pendingProductId) {
        this.pendingProductId = pendingProductId;
    }

    public void setPendingFromBranchId(String pendingFromBranchId) {
        this.pendingFromBranchId = pendingFromBranchId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }


    public String getPendingProductId() {
        return pendingProductId;
    }

    public String getPendingFromBranchId() {
        return pendingFromBranchId;
    }
}
