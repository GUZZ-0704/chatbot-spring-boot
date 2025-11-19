package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;

import java.util.List;

public interface GetHistoryUseCase {
    List<ChatMessage> getHistory(String sessionKey);
}
