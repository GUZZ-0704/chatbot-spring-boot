package com.example.chatbothexagonal.chatbot.domain.exception;

public class ChatSessionNotFoundException extends RuntimeException{
    public ChatSessionNotFoundException(String key) {
        super("No existe sesión con sessionKey: " + key);
    }
}
