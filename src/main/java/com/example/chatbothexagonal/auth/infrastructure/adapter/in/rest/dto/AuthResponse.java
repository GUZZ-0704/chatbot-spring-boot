package com.example.chatbothexagonal.auth.infrastructure.adapter.in.rest.dto;

public record AuthResponse(
        String tokenType,
        String accessToken,
        long expiresIn,
        Long userId,
        String email,
        String name
) {}
