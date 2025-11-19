package com.example.chatbothexagonal.chatbot.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class MessageId {

    private final UUID value;

    public MessageId(UUID value) {
        this.value = Objects.requireNonNull(value, "MessageId no puede ser null");
    }

    public UUID getValue() {
        return value;
    }
}