package hnqd.aparmentmanager.chatservice.service.impl;

import hnqd.aparmentmanager.chatservice.dto.ChatMessageRequestDto;
import hnqd.aparmentmanager.chatservice.model.ChatMessage;
import hnqd.aparmentmanager.chatservice.repository.IChatMessageRepo;
import hnqd.aparmentmanager.chatservice.service.IChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomMessageServiceImpl implements IChatMessageService {

    private final IChatMessageRepo chatMessageRepo;

    @Override
    public ChatMessage createChatMessage(ChatMessageRequestDto chatMessageReq) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(UUID.randomUUID());
        chatMessage.setUserId(chatMessageReq.getUserId());
        chatMessage.setContent(chatMessageReq.getContent());
        chatMessage.setChatRoomId(chatMessageReq.getChatRoomId());
        chatMessage.setCreatedAt(new Date());

        return chatMessageRepo.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessageByRoomId(UUID roomId) {
        return chatMessageRepo.findChatMessageByChatRoomId(roomId);
    }

}
