package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;

import java.util.List;

public interface LoadUserSessionsPort {
    List<ChatSession> loadSessionsByUserId(Long userId);
}
