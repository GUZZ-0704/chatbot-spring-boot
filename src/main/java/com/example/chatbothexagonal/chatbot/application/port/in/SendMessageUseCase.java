package com.example.chatbothexagonal.chatbot.application.port.in;

import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageResponse;

public interface SendMessageUseCase {
    ChatMessageResponse sendMessage(ChatMessageRequest request);
}
