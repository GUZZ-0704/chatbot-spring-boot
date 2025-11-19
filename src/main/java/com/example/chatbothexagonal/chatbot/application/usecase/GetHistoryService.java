package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.port.in.GetHistoryUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadHistoryPort;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadSessionPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetHistoryService implements GetHistoryUseCase {
    private final LoadSessionPort loadSessionPort;
    private final LoadHistoryPort loadHistoryPort;

    public GetHistoryService(LoadSessionPort loadSessionPort, LoadHistoryPort loadHistoryPort) {
        this.loadSessionPort = loadSessionPort;
        this.loadHistoryPort = loadHistoryPort;
    }

    @Override
    public List<ChatMessage> getHistory(String sessionKey) {
        ChatSession session = loadSessionPort.loadBySessionKey(sessionKey)
                .orElseThrow(() -> new IllegalArgumentException("Session not found for key: " + sessionKey));

        SessionId sessionId = session.getId();

        return loadHistoryPort.loadBySessionId(sessionId);
    }
}
