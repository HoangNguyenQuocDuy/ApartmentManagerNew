package hnqd.aparmentmanager.notificationservice.service.impl;

import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.notificationservice.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements IChatMessageService {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessageToRoom(ChatMessageRequestDto message, String roomId) {
        messagingTemplate.convertAndSend("/chat-rooms/" + roomId, message);
    }
}
