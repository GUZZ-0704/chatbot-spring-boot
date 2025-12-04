package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.port.in.DeleteUserSessionsUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.DeleteUserSessionsPort;
import com.example.chatbothexagonal.config.JwtAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteUserSessionsService implements DeleteUserSessionsUseCase {

    private final DeleteUserSessionsPort deletePort;

    public DeleteUserSessionsService(DeleteUserSessionsPort deletePort) {
        this.deletePort = deletePort;
    }

    @Override
    public void handle() {
        Long userId = extractUserId();
        if (userId == null)
            throw new IllegalArgumentException("Usuario no autenticado");

        deletePort.deleteSessionsByUserId(userId);
    }

    private Long extractUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        if (!(auth.getDetails() instanceof JwtAuthenticationFilter.AuthDetails details))
            return null;

        return details.userId();
    }
}

