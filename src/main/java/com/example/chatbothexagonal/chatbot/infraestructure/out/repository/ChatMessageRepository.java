package com.example.chatbothexagonal.chatbot.infraestructure.out.repository;

import com.example.chatbothexagonal.chatbot.infraestructure.out.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {
    List<ChatMessageEntity> findBySessionIdOrderByCreatedAtAsc(UUID sessionId);

    void deleteBySessionIdIn(List<UUID> sessionIds);
}
