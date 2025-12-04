package com.example.chatbothexagonal.auth.application.port.out;

public interface TokenEncoderPort {
    String generateAccessToken(Long userId, String name, String email);
    long accessTokenExpiresInSeconds();
}
