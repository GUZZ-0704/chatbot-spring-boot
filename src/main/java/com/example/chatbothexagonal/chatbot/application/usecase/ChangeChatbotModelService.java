package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.dto.ChangeModelRequest;
import com.example.chatbothexagonal.chatbot.application.port.in.ChangeChatbotModelUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.*;
import com.example.chatbothexagonal.chatbot.domain.exception.ChatSessionNotFoundException;
import com.example.chatbothexagonal.chatbot.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeChatbotModelService  implements ChangeChatbotModelUseCase {

    private final LoadSessionPort loadSessionPort;
    private final SaveSessionPort saveSessionPort;

    public ChangeChatbotModelService(
            LoadSessionPort loadSessionPort,
            SaveSessionPort saveSessionPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.saveSessionPort = saveSessionPort;
    }
    @Override
    public void changeModel(ChangeModelRequest request) {
        ChatSession session = loadSessionPort.loadBySessionKey(request.getSessionKey())
                .orElseThrow(() -> new ChatSessionNotFoundException(request.getSessionKey()));

        session.changeModel(request.getNewModel());

        saveSessionPort.save(session);
    }
}
