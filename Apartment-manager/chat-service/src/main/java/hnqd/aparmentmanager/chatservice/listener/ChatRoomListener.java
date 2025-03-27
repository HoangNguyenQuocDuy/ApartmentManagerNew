package hnqd.aparmentmanager.chatservice.listener;

import hnqd.aparmentmanager.chatservice.dto.ChatMessageRequestDto;
import hnqd.aparmentmanager.chatservice.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomListener {

    private final IChatMessageService chatMessageService;

    @RabbitListener(queues = "chatQueue")
    public void receiveMessage(ChatMessageRequestDto message) {
        chatMessageService.createChatMessage(message);
    }

}
