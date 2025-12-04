package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageResponse;
import com.example.chatbothexagonal.chatbot.application.port.in.SendMessageUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.*;
import com.example.chatbothexagonal.chatbot.domain.model.*;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import com.example.chatbothexagonal.config.JwtAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SendMessageService implements SendMessageUseCase {

    private final LoadSessionPort loadSessionPort;
    private final SaveSessionPort saveSessionPort;
    private final SaveMessagePort saveMessagePort;
    private final LoadHistoryPort loadHistoryPort;
    private final ChatbotInternalPort internalPort;
    private final ChatbotExternalN8nPort n8nPort;

    public SendMessageService(
            LoadSessionPort loadSessionPort,
            SaveSessionPort saveSessionPort,
            SaveMessagePort saveMessagePort,
            LoadHistoryPort loadHistoryPort,
            ChatbotInternalPort internalPort,
            ChatbotExternalN8nPort n8nPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.saveSessionPort = saveSessionPort;
        this.saveMessagePort = saveMessagePort;
        this.loadHistoryPort = loadHistoryPort;
        this.internalPort = internalPort;
        this.n8nPort = n8nPort;
    }

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {

        Long userId = extractUserId();

        ChatSession session = loadSessionPort.loadBySessionKey(request.getSessionKey())
                .orElseGet(() -> createNewSession(request.getSessionKey(), userId));

        SessionId sessionId = session.getId();

        ChatMessage msgUser = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.USER,
                request.getMessageText(),
                null,
                session.getActiveModel(),
                LocalDateTime.now()
        );
        saveMessagePort.save(msgUser);

        ChatbotResponse result;
        ChatbotModel modelUsed;

        try {
            if (session.getActiveModel() == ChatbotModel.INTERNAL_AI) {
                result = internalPort.process(request.getMessageText(), request.getSessionKey());

                if (result == null || result.getText() == null || result.getText().isBlank()) {
                    throw new RuntimeException("Respuesta interna vacía");
                }

                modelUsed = ChatbotModel.INTERNAL_AI;
            } else {
                result = n8nPort.process(request.getMessageText(), request.getSessionKey());
                modelUsed = ChatbotModel.N8N;
            }

        } catch (Exception ex) {
            result = n8nPort.process(request.getMessageText(), request.getSessionKey());
            modelUsed = ChatbotModel.N8N;
        }

        ChatMessage msgAssistant = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.ASSISTANT,
                result.getText(),
                result.getRawJson(),
                modelUsed,
                LocalDateTime.now()
        );
        saveMessagePort.save(msgAssistant);

        return new ChatMessageResponse(
                result.getText(),
                modelUsed,
                msgAssistant.getCreatedAt()
        );
    }

    private ChatSession createNewSession(String sessionKey, Long userId) {
        ChatSession newSession = new ChatSession(
                new SessionId(UUID.randomUUID()),
                userId,
                sessionKey,
                ChatbotModel.INTERNAL_AI,
                LocalDateTime.now()
        );

        saveSessionPort.save(newSession);
        return newSession;
    }

    private Long extractUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        if (!(auth.getDetails() instanceof JwtAuthenticationFilter.AuthDetails details))
            return null;

        return details.userId();
    }
}

