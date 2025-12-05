package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.*;
import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatSessionEntity;
import com.example.chatbothexagonal.chatbot.infraestructure.out.mapper.ChatbotEntityMapper;
import com.example.chatbothexagonal.chatbot.infraestructure.out.repository.ChatMessageRepository;
import com.example.chatbothexagonal.chatbot.infraestructure.out.repository.ChatSessionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ChatbotPersistenceAdapter implements
        LoadSessionPort,
        SaveSessionPort,
        SaveMessagePort,
        LoadHistoryPort,
        LoadUserSessionsPort,
        DeleteUserSessionsPort {

    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;

    public ChatbotPersistenceAdapter(ChatSessionRepository sessionRepo, ChatMessageRepository messageRepo) {
        this.sessionRepo = sessionRepo;
        this.messageRepo = messageRepo;
    }

    @Override
    public Optional<ChatSession> loadBySessionKey(String key) {
        return sessionRepo.findBySessionKey(key)
                .map(ChatbotEntityMapper::toDomain);
    }

    @Override
    public List<ChatSession> loadSessionsByUserId(Long userId) {
        return sessionRepo.findByUserId(userId).stream()
                .map(ChatbotEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteSessionsByUserId(Long userId) {
        List<ChatSessionEntity> sessions = sessionRepo.findByUserId(userId);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        List<UUID> sessionIds = sessions.stream()
                .map(ChatSessionEntity::getId)
                .toList();

        messageRepo.deleteBySessionIdIn(sessionIds);
        sessionRepo.deleteByUserId(userId);
    }

    @Override
    public void save(ChatSession session) {
        sessionRepo.save(ChatbotEntityMapper.toEntity(session));
    }

    @Override
    public void save(ChatMessage msg) {
        messageRepo.save(ChatbotEntityMapper.toEntity(msg));
    }

    @Override
    public List<ChatMessage> loadBySessionId(SessionId sessionId) {
        return messageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId.getValue())
                .stream()
                .map(ChatbotEntityMapper::toDomain)
                .toList();
    }
}
