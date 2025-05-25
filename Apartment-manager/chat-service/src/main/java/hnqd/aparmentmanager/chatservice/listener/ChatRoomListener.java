package hnqd.aparmentmanager.chatservice.listener;

import hnqd.aparmentmanager.chatservice.model.ChatRoom;
import hnqd.aparmentmanager.chatservice.repository.IChatRoomRepo;
import hnqd.aparmentmanager.chatservice.service.IChatMessageService;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatRoomListener {

    private final IChatMessageService chatMessageService;
    private final IChatRoomRepo chatRoomRepo;

    @RabbitListener(queues = "chatQueue.chat-service")
    @Transactional
    public void receiveMessage(ChatMessageRequestDto message) throws IOException {
        chatMessageService.createChatMessage(message);
        ChatRoom chatRoom = chatRoomRepo.findById(message.getChatRoomId())
                .orElseThrow(
                        () -> new CommonException.NotFoundException("Chat room not found")
                );
        chatRoom.setLastMessage(message.getContent());
        chatRoomRepo.save(chatRoom);
    }

}
