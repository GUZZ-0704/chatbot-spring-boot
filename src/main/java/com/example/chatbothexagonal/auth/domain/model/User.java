package com.example.chatbothexagonal.auth.domain.model;

import com.example.chatbothexagonal.auth.domain.valueObject.Email;

import java.time.Instant;

public final class User {

    private final Long id;
    private final String name;
    private final Email email;
    private final String passwordHash;
    private final Instant createdAt;
    private final Instant updatedAt;

    private User(Long id,
                 String name,
                 Email email,
                 String passwordHash,
                 Instant createdAt,
                 Instant updatedAt) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nombre requerido");

        if (email == null)
            throw new IllegalArgumentException("Email requerido");

        if (passwordHash == null || passwordHash.isBlank())
            throw new IllegalArgumentException("passwordHash requerido");

        if (createdAt == null || updatedAt == null)
            throw new IllegalArgumentException("Fechas requeridas");

        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNew(String name,
                                 Email email,
                                 String passwordHash,
                                 Instant now) {

        return new User(
                null,
                name,
                email,
                passwordHash,
                now,
                now
        );
    }

    public static User rehydrate(Long id,
                                 String name,
                                 Email email,
                                 String passwordHash,
                                 Instant createdAt,
                                 Instant updatedAt) {

        return new User(
                id,
                name,
                email,
                passwordHash,
                createdAt,
                updatedAt
        );
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public Email getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
