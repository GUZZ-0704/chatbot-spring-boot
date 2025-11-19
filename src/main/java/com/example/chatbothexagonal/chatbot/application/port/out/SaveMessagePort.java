package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;

public interface SaveMessagePort {
    void save(ChatMessage message);
}
