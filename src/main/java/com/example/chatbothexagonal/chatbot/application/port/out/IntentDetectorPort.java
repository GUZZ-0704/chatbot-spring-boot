package com.example.chatbothexagonal.chatbot.application.port.out;

import com.example.chatbothexagonal.chatbot.domain.model.ChatIntent;

public interface IntentDetectorPort {

    ChatIntent detectIntent(String messageText);
}
