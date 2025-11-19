package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;

import java.util.Optional;

public interface LoadSessionPort {
    Optional<ChatSession> loadBySessionKey(String sessionKey);
}
