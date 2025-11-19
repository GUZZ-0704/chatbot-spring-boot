package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;

import java.util.List;

public interface LoadHistoryPort {
    List<ChatMessage> loadBySessionId(SessionId sessionId);
}
