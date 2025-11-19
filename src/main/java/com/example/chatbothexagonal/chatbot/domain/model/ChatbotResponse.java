package com.example.chatbothexagonal.chatbot.domain.model;

public class ChatbotResponse {
    private final String text;
    private final String rawJson;

    public ChatbotResponse(String text, String rawJson) {
        this.text = text;
        this.rawJson = rawJson;
    }

    public String getText() {
        return text;
    }

    public String getRawJson() {
        return rawJson;
    }
}
