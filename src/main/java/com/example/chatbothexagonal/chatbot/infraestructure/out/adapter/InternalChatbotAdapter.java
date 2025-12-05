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
            log.info("Enviando petición a Gemini: {}", apiUrl);
            log.debug("Payload: {}", payload);
            response = rest.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            log.info("Respuesta de Gemini recibida. Status: {}", response.getStatusCode());
            log.debug("Body: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error al conectar con Gemini", e);
            return new ChatbotResponse(
                    "Ocurrió un error al conectar con Gemini: " + e.getMessage(),
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }

        String extractedText = extractAIResponse(response.getBody());
        log.info("Texto extraído: {}", extractedText);
        
        return new ChatbotResponse(
                extractedText,
                response.getBody().toString()
        );
    }

    private String buildProductKnowledge(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("Eres un asistente virtual amigable y útil. ");
        sb.append("Tu trabajo es ayudar a los usuarios con información sobre productos y responder sus preguntas de manera clara y concisa.\n\n");
        
        if (products != null && !products.isEmpty()) {
            sb.append("Estos son los productos disponibles:\n");
            for (Product p : products) {
                sb.append(String.format("- %s | Precio: $%.2f\n", p.getName(), p.getPrice()));
            }
            sb.append("\n");
        } else {
            sb.append("Actualmente no hay productos disponibles en el catálogo.\n\n");
        }
        
        sb.append("Responde siempre de manera amigable y profesional. ");
        sb.append("Si te preguntan por un producto que no está en la lista, indícalo amablemente.");
        
        log.debug("Contexto de productos construido: {} productos", products != null ? products.size() : 0);
        return sb.toString();
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
