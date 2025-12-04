package com.example.chatbothexagonal.auth.application.port.in;

import com.example.chatbothexagonal.auth.application.dto.AuthResult;

public interface LoginUseCase {
    AuthResult handle(String email, String rawPassword);
}
