
package com.slash.project.myfoli.domain.ai.presentation.controller;

import com.slash.project.myfoli.domain.ai.presentation.dto.ChatMessage;
import com.slash.project.myfoli.domain.ai.service.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AIChatController {
    private final AIChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage chat(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // 세션에서 userId 가져오기 (로그인 구현 시 적용)
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        String aiResponse = chatService.chat(message.getContent(), userId);

        return new ChatMessage("AI", aiResponse);
    }
}