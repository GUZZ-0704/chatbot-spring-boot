package com.example.chatbothexagonal.chatbot.infraestructure.out.entity;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
public class ChatSessionEntity {

    @Id
    private UUID id;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String sessionKey;

    @Enumerated(EnumType.STRING)
    private ChatbotModel activeModel;

    private LocalDateTime createdAt;

    protected ChatSessionEntity() {}

    public ChatSessionEntity(UUID id, Long userId, String sessionKey,
                             ChatbotModel activeModel, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.activeModel = activeModel;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public Long getUserId() { return userId; }
    public String getSessionKey() { return sessionKey; }
    public ChatbotModel getActiveModel() { return activeModel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
