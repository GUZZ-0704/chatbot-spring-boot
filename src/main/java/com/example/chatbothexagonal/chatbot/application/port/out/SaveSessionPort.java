package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;

public interface SaveSessionPort {
    void save(ChatSession session);
}
