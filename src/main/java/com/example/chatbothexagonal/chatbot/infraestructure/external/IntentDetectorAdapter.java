package com.example.chatbothexagonal.chatbot.infraestructure.external;

import com.example.chatbothexagonal.chatbot.application.port.out.IntentDetectorPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatIntent;
import org.springframework.stereotype.Component;

@Component
public class IntentDetectorAdapter implements IntentDetectorPort {

    @Override
    public ChatIntent detectIntent(String text) {
        text = text.toLowerCase();

        if (text.matches(".*(si|sí|dale|ok|confirmo|correcto).*")) {
            return ChatIntent.CONFIRM_YES;
        }

        if (text.matches(".*(no|nunca|negativo).*")) {
            return ChatIntent.CONFIRM_NO;
        }

        if (text.contains("enviar") || text.contains("mandar") || text.contains("transferir")) {
            return ChatIntent.REQUEST_TRANSFER;
        }

        if (text.matches(".*(tienen|hay|busco|quiero|producto|sku).*")) {
            return ChatIntent.SEARCH_PRODUCT;
        }

        return ChatIntent.GENERAL_CONVERSATION;
    }
}