package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;

import java.util.List;

public interface GetUserSessionsUseCase {
    List<ChatSession> handle();
}
