package com.example.chatbothexagonal.auth.application.dto;

public record AuthResult(
        String tokenType,
        String accessToken,
        long expiresIn,
        Long userId,
        String email,
        String name
) {}
