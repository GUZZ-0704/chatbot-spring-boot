package com.example.chatbothexagonal.chatbot.application.usecase;

import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageResponse;
import com.example.chatbothexagonal.chatbot.application.port.in.SendMessageUseCase;
import com.example.chatbothexagonal.chatbot.application.port.out.*;
import com.example.chatbothexagonal.chatbot.domain.model.*;
import com.example.chatbothexagonal.chatbot.domain.valueobject.MessageId;
import com.example.chatbothexagonal.chatbot.domain.valueobject.SessionId;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.BranchStockResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.ProductResponseDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.external.inventory.dto.TransferRequestDTO;
import com.example.chatbothexagonal.config.JwtAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SendMessageService implements SendMessageUseCase {

    private final LoadSessionPort loadSessionPort;
    private final SaveSessionPort saveSessionPort;
    private final SaveMessagePort saveMessagePort;

    private final IntentDetectorPort intentDetectorPort;
    private final InventoryExternalPort inventoryExternalPort;

    private final ChatbotInternalPort internalPort;
    private final ChatbotExternalN8nPort n8nPort;

    public SendMessageService(
            LoadSessionPort loadSessionPort,
            SaveSessionPort saveSessionPort,
            SaveMessagePort saveMessagePort,
            IntentDetectorPort intentDetectorPort,
            InventoryExternalPort inventoryExternalPort,
            ChatbotInternalPort internalPort,
            ChatbotExternalN8nPort n8nPort
    ) {
        this.loadSessionPort = loadSessionPort;
        this.saveSessionPort = saveSessionPort;
        this.saveMessagePort = saveMessagePort;
        this.intentDetectorPort = intentDetectorPort;
        this.inventoryExternalPort = inventoryExternalPort;
        this.internalPort = internalPort;
        this.n8nPort = n8nPort;
    }

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {

        Long userId = extractUserId();

        ChatSession session = loadSessionPort.loadBySessionKey(request.getSessionKey())
                .orElseGet(() -> createNewSession(request.getSessionKey(), userId));

        SessionId sessionId = session.getId();

        ChatMessage msgUser = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.USER,
                request.getMessageText(),
                null,
                session.getActiveModel(),
                LocalDateTime.now()
        );
        saveMessagePort.save(msgUser);

        ChatIntent intent = intentDetectorPort.detectIntent(request.getMessageText());

        if (session.getPendingAction() != ChatPendingAction.NONE) {
            String result = handlePendingAction(session, request, intent);
            saveSessionPort.save(session);
            return createAssistantResponse(result, session, sessionId);
        }

        if (intent == ChatIntent.SEARCH_PRODUCT ||
                intent == ChatIntent.REQUEST_TRANSFER ||
                intent == ChatIntent.CROSS_BRANCH_SEARCH ||
                intent == ChatIntent.CONFIRM_YES ||
                intent == ChatIntent.CONFIRM_NO) {

            String response = processInventoryFlow(session, request, intent);
            saveSessionPort.save(session);
            return createAssistantResponse(response, session, sessionId);
        }

        ChatbotResponse result;

        try {
            result = internalPort.process(request.getMessageText(), request.getSessionKey());
            if (result == null || result.getText() == null || result.getText().isBlank()) {
                throw new RuntimeException("Empty internal response");
            }
            session.changeModel(ChatbotModel.INTERNAL_AI);
        } catch (Exception ex) {
            result = n8nPort.process(request.getMessageText(), request.getSessionKey());
            session.changeModel(ChatbotModel.N8N);
        }

        return createAssistantResponse(result.getText(), session, sessionId);
    }

    private String processInventoryFlow(ChatSession session, ChatMessageRequest req, ChatIntent intent) {
        switch (intent) {
            case SEARCH_PRODUCT:
                return handleSearchProduct(session, req);
            case CONFIRM_YES:
            case CONFIRM_NO:
                return "No tengo acciones pendientes.";
            default:
                return "¿Qué producto necesitas?";
        }
    }

    private String handleSearchProduct(ChatSession session, ChatMessageRequest req) {

        // ⭐ EXTRAEMOS SOLO EL PRODUCTO usando IA
        String keyword = extractProductKeywordAI(req.getMessageText(), req.getSessionKey());
        if (keyword.equalsIgnoreCase("NONE")) {
            return "No entendí qué producto estás buscando. ¿Puedes decirlo nuevamente?";
        }

        UUID branchId = UUID.fromString(req.getBranchId());

        List<ProductResponseDTO> products = inventoryExternalPort.searchProducts(keyword);
        if (products.isEmpty()) {
            return "No encontré productos relacionados a: " + keyword;
        }

        ProductResponseDTO product = products.get(0);

        List<BranchStockResponseDTO> branchStock = inventoryExternalPort.listStockByBranch(branchId);

        Optional<BranchStockResponseDTO> stockInBranch =
                branchStock.stream()
                        .filter(s -> s.productId().equals(product.id()))
                        .findFirst();

        if (stockInBranch.isPresent() && stockInBranch.get().quantity() > 0) {
            return "Sí, tenemos stock de " + product.name() + " en esta sucursal.";
        }

        session.setPendingAction(ChatPendingAction.CONFIRM_SEARCH_OTHER_BRANCHES);
        session.setPendingProductId(product.id().toString());
        session.setPendingFromBranchId(branchId.toString());

        return "No tenemos stock aquí. ¿Quieres buscar en otras sucursales?";
    }

    private String handlePendingAction(ChatSession session, ChatMessageRequest req, ChatIntent intent) {
        switch (session.getPendingAction()) {
            case CONFIRM_SEARCH_OTHER_BRANCHES:
                return handleConfirmSearchOtherBranches(session, intent);
            case CONFIRM_TRANSFER:
                return handleConfirmTransfer(session, intent, req);
            default:
                session.clearPending();
                return "Acción pendiente inválida.";
        }
    }

    private String handleConfirmSearchOtherBranches(ChatSession session, ChatIntent intent) {

        if (intent == ChatIntent.CONFIRM_NO) {
            session.clearPending();
            return "Perfecto, no buscaremos en otras sucursales.";
        }

        if (intent != ChatIntent.CONFIRM_YES) {
            return "Por favor responde sí o no.";
        }

        UUID productId = UUID.fromString(session.getPendingProductId());
        List<BranchStockResponseDTO> stocks = inventoryExternalPort.listStockByProduct(productId);

        Optional<BranchStockResponseDTO> available =
                stocks.stream()
                        .filter(s -> s.quantity() > 0)
                        .findFirst();

        if (available.isEmpty()) {
            session.clearPending();
            return "El producto no está disponible en ninguna otra sucursal.";
        }

        BranchStockResponseDTO stock = available.get();

        session.setPendingAction(ChatPendingAction.CONFIRM_TRANSFER);
        session.setPendingFromBranchId(stock.branchId().toString());

        return "El producto está disponible en " + stock.branchName() +
                ". ¿Quieres solicitar el envío a tu sucursal?";
    }

    private String handleConfirmTransfer(ChatSession session, ChatIntent intent, ChatMessageRequest req) {

        if (intent == ChatIntent.CONFIRM_NO) {
            session.clearPending();
            return "Perfecto. No realizaremos la transferencia.";
        }

        if (intent != ChatIntent.CONFIRM_YES) {
            return "Por favor responde sí o no.";
        }

        UUID productId = UUID.fromString(session.getPendingProductId());
        UUID fromBranch = UUID.fromString(session.getPendingFromBranchId());
        UUID toBranch = UUID.fromString(req.getBranchId());

        List<BranchStockResponseDTO> stockList = inventoryExternalPort.listStockByProduct(productId);

        BranchStockResponseDTO origin = stockList.stream()
                .filter(s -> s.branchId().equals(fromBranch))
                .findFirst()
                .orElse(null);

        if (origin == null) {
            session.clearPending();
            return "Error obteniendo el stock para la transferencia.";
        }

        TransferRequestDTO dto = new TransferRequestDTO(
                productId,
                origin.batchId(),
                fromBranch,
                toBranch,
                1,
                "Transferencia solicitada desde chatbot",
                "user-" + session.getUserId()
        );

        inventoryExternalPort.requestTransfer(dto);

        session.clearPending();
        return "¡Listo! La transferencia fue solicitada.";
    }

    private ChatMessageResponse createAssistantResponse(String text, ChatSession session, SessionId sessionId) {

        ChatMessage msgAssistant = new ChatMessage(
                new MessageId(UUID.randomUUID()),
                sessionId,
                MessageRole.ASSISTANT,
                text,
                null,
                session.getActiveModel(),
                LocalDateTime.now()
        );

        saveMessagePort.save(msgAssistant);

        return new ChatMessageResponse(
                text,
                session.getActiveModel(),
                msgAssistant.getCreatedAt()
        );
    }

    private ChatSession createNewSession(String sessionKey, Long userId) {

        ChatSession newSession = new ChatSession(
                new SessionId(UUID.randomUUID()),
                userId,
                sessionKey,
                ChatbotModel.INTERNAL_AI,
                LocalDateTime.now()
        );

        newSession.setPendingAction(ChatPendingAction.NONE);

        saveSessionPort.save(newSession);
        return newSession;
    }

    private Long extractUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        if (!(auth.getDetails() instanceof JwtAuthenticationFilter.AuthDetails details))
            return null;
        return details.userId();
    }

    private String extractProductKeywordAI(String message, String sessionKey) {

        String prompt = """
                Extrae SOLO el nombre del producto mencionado en el siguiente texto.
                No agregues explicaciones, no agregues frases extras.
                Devuelve solo el nombre del producto tal como lo escribiría un usuario común.
                Si no hay producto, responde exactamente: NONE.

                Texto: "%s"
                Respuesta:
                """.formatted(message);

        try {
            ChatbotResponse result = internalPort.process(prompt, sessionKey);

            if (result == null || result.getText() == null) return "NONE";

            String cleaned = result.getText().trim().toLowerCase();

            cleaned = cleaned.split("\n")[0].trim();

            return cleaned;
        } catch (Exception e) {
            return "NONE";
        }
    }
}
