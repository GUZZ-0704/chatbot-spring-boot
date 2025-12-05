package com.example.chatbothexagonal.chatbot.infraestructure.out.mapper;

import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatMessageEntity;
import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatSessionEntity;

import java.util.UUID;

public class ChatbotEntityMapper {

    public static ChatSession toDomain(ChatSessionEntity e) {

        ChatSession domain = new ChatSession(
                new SessionId(e.getId()),
                e.getUserId(),
                e.getSessionKey(),
                e.getActiveModel(),
                e.getCreatedAt()
        );

        domain.setPendingAction(e.getPendingAction());
        domain.setPendingProductId(e.getPendingProductId());
        domain.setPendingFromBranchId(e.getPendingFromBranchId());

        return domain;
    }


    public static ChatSessionEntity toEntity(ChatSession s) {
        return new ChatSessionEntity(
                s.getId().getValue(),
                s.getUserId(),
                s.getSessionKey(),
                s.getActiveModel(),
                s.getCreatedAt(),
                s.getPendingAction(),
                s.getPendingProductId(),
                s.getPendingFromBranchId()
        );
    }


    public static ChatMessage toDomain(ChatMessageEntity e) {
        return new ChatMessage(
                new MessageId(e.getId()),
                new SessionId(e.getSessionId()),
                e.getRole(),
                e.getMessageText(),
                e.getRawJson(),
                e.getModelUsed(),
                e.getCreatedAt()
        );
    }


    public static ChatMessageEntity toEntity(ChatMessage msg) {
        return new ChatMessageEntity(
                msg.getId().getValue(),
                msg.getSessionId().getValue(),
                msg.getRole(),
                msg.getMessageText(),
                msg.getRawJson(),
                msg.getModelUsed(),
                msg.getCreatedAt()
        );
    }
}

