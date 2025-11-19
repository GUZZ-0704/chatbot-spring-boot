package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;

public interface ChatbotInternalPort {
    ChatbotResponse process(String message, String sessionKey);
}
