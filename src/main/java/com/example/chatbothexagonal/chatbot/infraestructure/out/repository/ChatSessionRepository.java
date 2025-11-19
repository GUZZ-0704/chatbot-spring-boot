package com.example.chatbothexagonal.chatbot.infraestructure.out.repository;

import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, UUID> {
    Optional<ChatSessionEntity> findBySessionKey(String sessionKey);
}
