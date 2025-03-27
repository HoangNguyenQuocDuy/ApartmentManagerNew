package hnqd.aparmentmanager.notificationservice.controller;

import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketHandler {

    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("/chat")
    public void sendMessage(ChatMessageRequestDto message) {
        rabbitTemplate.convertAndSend("chatExchange", "G8wMk8fKtQ", message);
    }

}
