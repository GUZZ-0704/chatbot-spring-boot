package com.example.chatbothexagonal.chatbot.infraestructure.out.adapter;

import com.example.chatbothexagonal.chatbot.application.port.out.ChatbotInternalPort;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadHistoryPort;
import com.example.chatbothexagonal.chatbot.application.port.out.LoadSessionPort;
import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.domain.model.ChatSession;
import com.example.chatbothexagonal.chatbot.domain.model.ChatbotResponse;
import com.example.chatbothexagonal.chatbot.domain.model.MessageRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InternalChatbotAdapter implements ChatbotInternalPort {

    private static final Logger log = LoggerFactory.getLogger(InternalChatbotAdapter.class);
    private final RestTemplate rest = new RestTemplate();
    private final LoadSessionPort loadSessionPort;
    private final LoadHistoryPort loadHistoryPort;

    @Value("${gemini.url}")
    private String apiUrl;

    public InternalChatbotAdapter(
            LoadSessionPort loadSessionPort,
            LoadHistoryPort loadHistoryPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.loadHistoryPort = loadHistoryPort;
    }

    @Override
    public ChatbotResponse process(String message, String sessionKey) {

        ChatSession session = loadSessionPort.loadBySessionKey(sessionKey)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionKey));

        List<ChatMessage> history = loadHistoryPort.loadBySessionId(session.getId());
        List<Map<String, Object>> contents = new ArrayList<>();

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
            log.info("Enviando petición a Gemini: {}", apiUrl);
            log.debug("Payload: {}", payload);
            response = rest.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            log.info("Respuesta de Gemini recibida. Status: {}", response.getStatusCode());
            log.debug("Body: {}", response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String extractedText = extractAIResponse(response.getBody());
        log.info("Texto extraído: {}", extractedText);
        
        return new ChatbotResponse(
                extractedText,
                response.getBody().toString()
        );
    }

    private String extractAIResponse(Map body) {
        try {
            // Validar que exista candidates
            if (body == null || !body.containsKey("candidates")) {
                return "La respuesta del modelo no contiene candidatos.";
            }

            List<Map> candidates = (List<Map>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "La lista de candidatos está vacía.";
            }

            Map candidate = candidates.get(0);
            if (candidate == null || !candidate.containsKey("content")) {
                return "El candidato no contiene contenido.";
            }

            Map content = (Map) candidate.get("content");
            if (content == null || !content.containsKey("parts")) {
                return "El contenido no tiene partes (parts).";
            }

            List<Map> parts = (List<Map>) content.get("parts");
            if (parts == null || parts.isEmpty()) {
                return "La lista de partes está vacía.";
            }

            Map firstPart = parts.get(0);
            if (firstPart == null || !firstPart.containsKey("text")) {
                return "La primera parte no contiene texto.";
            }

            Object textObj = firstPart.get("text");
            if (textObj == null) {
                return "El texto es nulo.";
            }

            String text = textObj.toString().trim();
            if (text.isEmpty()) {
                return "El texto está vacío.";
            }

            return text;

        } catch (ClassCastException e) {
            return "Error de tipo al parsear la respuesta: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al interpretar la respuesta: " + e.getMessage();
        }
    }
}
