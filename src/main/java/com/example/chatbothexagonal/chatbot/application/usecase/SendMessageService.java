package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageResponse;
import com.example.chatbothexagonal.chatbot.application.port.in.SendMessageUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.*;
import com.example.chatbothexagonal.chatbot.domain.exception.ChatSessionNotFoundException;
import com.example.chatbothexagonal.chatbot.domain.model.*;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SendMessageService implements SendMessageUseCase {
    private final LoadSessionPort loadSessionPort;
    private final SaveMessagePort saveMessagePort;
    private final LoadHistoryPort loadHistoryPort;
    private final ChatbotInternalPort internalPort;
    private final ChatbotExternalN8nPort n8nPort;

    public SendMessageService(
            LoadSessionPort loadSessionPort,
            SaveMessagePort saveMessagePort,
            LoadHistoryPort loadHistoryPort,
            ChatbotInternalPort internalPort,
            ChatbotExternalN8nPort n8nPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.saveMessagePort = saveMessagePort;
        this.loadHistoryPort = loadHistoryPort;
        this.internalPort = internalPort;
        this.n8nPort = n8nPort;
    }

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        ChatSession session = loadSessionPort.loadBySessionKey(request.getSessionKey())
                .orElseThrow(() -> new ChatSessionNotFoundException(request.getSessionKey()));

        SessionId sessionId = session.getId();

        ChatMessage msgUser = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.USER,
                request.getMessageText(),
                null,
                ChatbotModel.UNKNOWN,
                LocalDateTime.now()
        );
        saveMessagePort.save(msgUser);

        ChatbotResponse responseModel = switch (session.getActiveModel()) {
            case INTERNAL_AI -> internalPort.process(request.getMessageText(), request.getSessionKey());
            case N8N -> n8nPort.process(request.getMessageText(), request.getSessionKey());
            default -> throw new RuntimeException("Modelo no soportado");
        };

        ChatMessage msgAssistant = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.ASSISTANT,
                responseModel.getText(),
                responseModel.getRawJson(),
                session.getActiveModel(),
                LocalDateTime.now()
        );
        saveMessagePort.save(msgAssistant);

        return new ChatMessageResponse(
                responseModel.getText(),
                session.getActiveModel(),
                msgAssistant.getCreatedAt()
        );
    }
}
