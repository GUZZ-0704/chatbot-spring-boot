package com.example.chatbothexagonal.chatbot.application.port.out;

public interface DeleteUserSessionsPort {
    void deleteSessionsByUserId(Long userId);
}
