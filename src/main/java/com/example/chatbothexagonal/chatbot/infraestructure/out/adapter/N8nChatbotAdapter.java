package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.ChatbotExternalN8nPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class N8nChatbotAdapter implements ChatbotExternalN8nPort {
    private final RestTemplate rest = new RestTemplate();

    @Value("${n8n.webhook.url}")
    private String webhookUrl;



    @Override
    public ChatbotResponse process(String message, String sessionKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("messageText", message);
        payload.put("sessionKey", sessionKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response;
        try{
            response = rest.exchange(webhookUrl, HttpMethod.POST, entity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ChatbotResponse(
                    "Error al conectar con N8N"+ e.getMessage(),
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );

        }
        return new ChatbotResponse(
                extractN8nResponse(response.getBody()),
                response.getBody().toString()
        );
    }
    private String extractN8nResponse(Map body) {
        if (body != null && body.containsKey("response")) {
            return body.get("response").toString();
        }
        return "No se pudo obtener la respuesta de N8N";
    }
}