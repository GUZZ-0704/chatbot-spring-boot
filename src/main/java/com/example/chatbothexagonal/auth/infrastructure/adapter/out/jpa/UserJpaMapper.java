package com.example.chatbothexagonal.auth.infrastructure.adapter.out.jpa;

import com.example.chatbothexagonal.auth.domain.model.User;
import com.example.chatbothexagonal.auth.domain.valueObject.Email;

public final class UserJpaMapper {

    private UserJpaMapper() {}

    public static User toDomain(UserEntity entity) {
        return User.rehydrate(
                entity.getId(),
                entity.getName(),
                Email.of(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static UserEntity toEntity(User domain) {
        UserEntity e = new UserEntity();

        if (domain.getId() != null) {
            e.setId(domain.getId());
        }

        e.setName(domain.getName());
        e.setEmail(domain.getEmail().getValue());
        e.setPasswordHash(domain.getPasswordHash());
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());

        return e;
    }
}
