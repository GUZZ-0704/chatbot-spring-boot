package com.example.chatbothexagonal.auth.application.port.out;

import com.example.chatbothexagonal.auth.domain.model.User;
import com.example.chatbothexagonal.auth.domain.valueObject.Email;

import java.util.Optional;

public interface LoadUserPort {

    Optional<User> findByEmail(Email email);

    Optional<User> findById(Long id);

    boolean existsByEmail(Email email);
}
