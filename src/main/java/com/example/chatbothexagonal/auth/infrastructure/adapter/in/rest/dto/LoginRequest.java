package com.example.chatbothexagonal.auth.infrastructure.adapter.in.rest.dto;

import com.example.chatbothexagonal.common.validation.NoWhitespace;
import com.example.chatbothexagonal.common.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @ValidEmail @NoWhitespace String email,
        @NotBlank String password
) {}
