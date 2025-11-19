package com.example.chatbothexagonal.chatbot.infraestructure.out.entity;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotModel;
import com.example.chatbothexagonal.chatbot.domain.model.MessageRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    private MessageRole role;

    @Column(nullable = false, length = 4000)
    private String messageText;

    @Column(columnDefinition = "TEXT")
    private String rawJson;

    @Enumerated(EnumType.STRING)
    private ChatbotModel modelUsed;

    private LocalDateTime createdAt;

    protected ChatMessageEntity() {}

    public ChatMessageEntity(UUID id, UUID sessionId, MessageRole role,
                             String messageText, String rawJson,
                             ChatbotModel modelUsed, LocalDateTime createdAt) {

        this.id = id;
        this.sessionId = sessionId;
        this.role = role;
        this.messageText = messageText;
        this.rawJson = rawJson;
        this.modelUsed = modelUsed;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public MessageRole getRole() { return role; }
    public String getMessageText() { return messageText; }
    public String getRawJson() { return rawJson; }
    public ChatbotModel getModelUsed() { return modelUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}