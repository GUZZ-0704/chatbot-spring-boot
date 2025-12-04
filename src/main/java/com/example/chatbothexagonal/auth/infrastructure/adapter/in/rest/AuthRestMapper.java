package com.example.chatbothexagonal.auth.infrastructure.adapter.in.rest;

import com.example.chatbothexagonal.auth.application.dto.AuthResult;
import com.example.chatbothexagonal.auth.infrastructure.adapter.in.rest.dto.AuthResponse;

final class AuthRestMapper {
    private AuthRestMapper(){}
    static AuthResponse toResponse(AuthResult r) {
        return new AuthResponse(r.tokenType(), r.accessToken(), r.expiresIn(),
                r.userId(), r.email(), r.name());
    }
}
