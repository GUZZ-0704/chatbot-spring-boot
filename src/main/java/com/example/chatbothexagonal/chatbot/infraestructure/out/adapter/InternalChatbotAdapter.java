package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.ChatbotInternalPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;
import org.springframework.stereotype.Component;

@Component
public class InternalChatbotAdapter implements ChatbotInternalPort {
    @Override
    public ChatbotResponse process(String message, String sessionKey) {
        // TODO: implementación futura (Gemini, OpenAI, etc.)
        return new ChatbotResponse(
                "Respuesta interna MOCK",
                "{\"mock\": true}"
        );
    }
}
