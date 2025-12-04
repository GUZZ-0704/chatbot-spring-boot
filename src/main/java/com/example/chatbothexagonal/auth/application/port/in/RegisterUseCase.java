package com.example.chatbothexagonal.auth.application.port.in;

import com.example.chatbothexagonal.auth.application.dto.AuthResult;

public interface RegisterUseCase {
    AuthResult handle(String email, String name, String rawPassword);
}
