package com.example.chatbothexagonal.auth.infrastructure.adapter.out.jpa;

import com.example.chatbothexagonal.auth.application.port.out.LoadUserPort;
import com.example.chatbothexagonal.auth.application.port.out.SaveUserPort;
import com.example.chatbothexagonal.auth.domain.model.User;
import com.example.chatbothexagonal.auth.domain.valueObject.Email;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements LoadUserPort, SaveUserPort {

    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User save(User user) {
        var saved = jpa.save(UserJpaMapper.toEntity(user));
        return UserJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpa.findByEmail(email.getValue())
                .map(UserJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id)
                .map(UserJpaMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmail(email.getValue());
    }
}
