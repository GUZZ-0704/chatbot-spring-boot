package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.ChatbotInternalPort;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadHistoryPort;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadSessionPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;
import com.example.chatbothexagonal.chatbot.domain.model.MessageRole;
import com.example.chatbothexagonal.product.application.port.out.LoadProductsPort;
import com.example.chatbothexagonal.product.domain.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InternalChatbotAdapter implements ChatbotInternalPort {

    private final RestTemplate rest = new RestTemplate();
    private final LoadSessionPort loadSessionPort;
    private final LoadHistoryPort loadHistoryPort;
    private final LoadProductsPort loadProductsPort;

    @Value("${gemini.url}")
    private String apiUrl;

    public InternalChatbotAdapter(
            LoadSessionPort loadSessionPort,
            LoadHistoryPort loadHistoryPort,
            LoadProductsPort loadProductsPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.loadHistoryPort = loadHistoryPort;
        this.loadProductsPort = loadProductsPort;
    }

    @Override
    public ChatbotResponse process(String message, String sessionKey) {

        ChatSession session = loadSessionPort.loadBySessionKey(sessionKey)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionKey));

        List<ChatMessage> history = loadHistoryPort.loadBySessionId(session.getId());
        List<Product> products = loadProductsPort.loadAll();

        List<Map<String, Object>> contents = new ArrayList<>();

        contents.add(Map.of(
                "role", "model",
                "parts", List.of(Map.of("text", buildProductKnowledge(products)))
        ));

        for (ChatMessage msg : history) {
            if (msg.getMessageText() == null) continue;

            contents.add(Map.of(
                    "role", msg.getRole() == MessageRole.USER ? "user" : "model",
                    "parts", List.of(Map.of("text", msg.getMessageText()))
            ));
        }

        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", message))
        ));

        Map<String, Object> payload = Map.of("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response;

        try {
            response = rest.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ChatbotResponse(
                    "Ocurrió un error al conectar con Gemini: " + e.getMessage(),
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }

        return new ChatbotResponse(
                extractAIResponse(response.getBody()),
                response.getBody().toString()
        );
    }

    private String buildProductKnowledge(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("Eres un experto en productos. Usa únicamente la siguiente lista:\n");

        for (Product p : products) {
            sb.append(String.format("- %s | Precio: %.2f\n", p.getName(), p.getPrice()));
        }

        return sb.toString();
    }

    private String extractAIResponse(Map body) {
        try {
            List<Map> candidates = (List<Map>) body.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            return "No pude interpretar la respuesta del modelo.";
        }
    }
}
