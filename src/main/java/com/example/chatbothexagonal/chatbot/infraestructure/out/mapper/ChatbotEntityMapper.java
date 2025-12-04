package com.example.chatbothexagonal.chatbot.infraestructure.out.mapper;

import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatMessageEntity;
import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatSessionEntity;

public class ChatbotEntityMapper {

    public static ChatSession toDomain(ChatSessionEntity e) {
        return new ChatSession(
                new SessionId(e.getId()),
                e.getUserId(),
                e.getSessionKey(),
                e.getActiveModel(),
                e.getCreatedAt()
        );
    }

    public static ChatSessionEntity toEntity(ChatSession session) {
        return new ChatSessionEntity(
                session.getId().getValue(),
                session.getUserId(),
                session.getSessionKey(),
                session.getActiveModel(),
                session.getCreatedAt()
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
