package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.application.dto.ChangeModelRequest;

public interface ChangeChatbotModelUseCase {
    void changeModel(ChangeModelRequest request);
}
