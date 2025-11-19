package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.ChatbotExternalN8nPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;
import org.springframework.stereotype.Component;

@Component
public class N8nChatbotAdapter implements ChatbotExternalN8nPort {

    @Override
    public ChatbotResponse process(String message, String sessionKey) {
        // TODO: implementación futura
        return new ChatbotResponse(
                "Respuesta N8N MOCK",
                "{\"mock\": true}"
        );
    }
}