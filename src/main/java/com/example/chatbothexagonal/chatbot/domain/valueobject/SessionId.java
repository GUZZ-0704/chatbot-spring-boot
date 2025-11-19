package com.example.chatbothexagonal.chatbot.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class SessionId {

    private final UUID value;

    public SessionId(UUID value) {
        this.value = Objects.requireNonNull(value, "SessionId no puede ser null");
    }

    public UUID getValue() {
        return value;
    }
}