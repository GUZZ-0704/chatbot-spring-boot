package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.port.in.GetUserSessionsUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadUserSessionsPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.config.JwtAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetUserSessionsService implements GetUserSessionsUseCase {

    private final LoadUserSessionsPort loadUserSessionsPort;

    public GetUserSessionsService(LoadUserSessionsPort loadUserSessionsPort) {
        this.loadUserSessionsPort = loadUserSessionsPort;
    }

    @Override
    public List<ChatSession> handle() {
        Long userId = extractUserId();
        if (userId == null)
            throw new IllegalArgumentException("Usuario no autenticado");

        return loadUserSessionsPort.loadSessionsByUserId(userId);
    }

    private Long extractUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        if (!(auth.getDetails() instanceof JwtAuthenticationFilter.AuthDetails details))
            return null;

        return details.userId();
    }
}
