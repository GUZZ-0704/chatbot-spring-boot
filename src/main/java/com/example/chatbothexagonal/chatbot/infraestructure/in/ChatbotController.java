package com.example.chatbothexagonal.chatbot.infraestructure.in;


import com.example.chatbothexagonal.chatbot.application.dto.ChangeModelRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageRequest;
import com.example.chatbothexagonal.chatbot.application.dto.ChatMessageResponse;
import com.example.chatbothexagonal.chatbot.application.port.in.ChangeChatbotModelUseCase;
import com.example.chatbothexagonal.chatbot.application.port.in.GetHistoryUseCase;
import com.example.chatbothexagonal.chatbot.application.port.in.SendMessageUseCase;
import com.example.chatbothexagonal.chatbot.domain.model.ChatMessage;
import com.example.chatbothexagonal.chatbot.infraestructure.in.dto.ChangeModelRequestDTO;
import com.example.chatbothexagonal.chatbot.infraestructure.in.dto.ChatMessageRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    private final SendMessageUseCase sendMessageUseCase;
    private final GetHistoryUseCase getHistoryUseCase;
    private final ChangeChatbotModelUseCase changeModelUseCase;

    public ChatbotController(
            SendMessageUseCase sendMessageUseCase,
            GetHistoryUseCase getHistoryUseCase,
            ChangeChatbotModelUseCase changeModelUseCase
    ) {
        this.sendMessageUseCase = sendMessageUseCase;
        this.getHistoryUseCase = getHistoryUseCase;
        this.changeModelUseCase = changeModelUseCase;
    }

    @PostMapping("/send")
    public ChatMessageResponse sendMessage(@Valid @RequestBody ChatMessageRequestDTO dto) {
        ChatMessageRequest request =
                new ChatMessageRequest(dto.getSessionKey(), dto.getMessageText());
        return sendMessageUseCase.sendMessage(request);
    }

    @GetMapping("/history/{sessionKey}")
    public List<ChatMessage> getHistory(@PathVariable String sessionKey) {
        return getHistoryUseCase.getHistory(sessionKey);
    }

    @PostMapping("/change-model")
    public void changeModel(@Valid @RequestBody ChangeModelRequestDTO dto) {
        ChangeModelRequest req = new ChangeModelRequest(dto.getSessionKey(), dto.getNewModel());
        changeModelUseCase.changeModel(req);
    }
}
