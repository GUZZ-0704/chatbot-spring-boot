package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;

public interface ChatbotExternalN8nPort {
    ChatbotResponse process(String message, String sessionKey);
}
