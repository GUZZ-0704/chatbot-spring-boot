package com.example.chatbothexagonal.auth.infrastructure.adapter.in.rest.dto;

import com.example.chatbothexagonal.common.validation.NoWhitespace;
import com.example.chatbothexagonal.common.validation.StrongPassword;
import com.example.chatbothexagonal.common.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @ValidEmail @NoWhitespace String email,
        @NotBlank @NoWhitespace @Size(max = 255) String name,
        @NotBlank @StrongPassword String password
) {}
