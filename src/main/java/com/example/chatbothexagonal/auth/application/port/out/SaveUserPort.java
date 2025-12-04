package com.example.chatbothexagonal.auth.application.port.out;


import com.example.chatbothexagonal.auth.domain.model.User;

public interface SaveUserPort {
    User save(User credential);
}
