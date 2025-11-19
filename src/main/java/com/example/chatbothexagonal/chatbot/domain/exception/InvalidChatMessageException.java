package com.example.chatbothexagonal.chatbot.domain.exception;

public class InvalidChatMessageException extends RuntimeException {
    public InvalidChatMessageException(String message) {
        super(message);
    }
}
