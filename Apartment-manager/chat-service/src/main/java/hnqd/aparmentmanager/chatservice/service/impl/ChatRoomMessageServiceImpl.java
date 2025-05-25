package hnqd.aparmentmanager.chatservice.service.impl;

import hnqd.aparmentmanager.chatservice.model.ChatMessage;
import hnqd.aparmentmanager.chatservice.repository.IChatMessageRepo;
import hnqd.aparmentmanager.chatservice.service.IChatMessageService;
import hnqd.aparmentmanager.common.Enum.EChatType;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.common.utils.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomMessageServiceImpl implements IChatMessageService {

    private final IChatMessageRepo chatMessageRepo;
    private final UploadImage uploadImage;

    @Override
    public ChatMessage createChatMessage(ChatMessageRequestDto chatMessageReq) throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(chatMessage.getType());

        switch (EChatType.safeValueOfName(chatMessageReq.getType())) {
            case MESSAGE:
                chatMessage.setContent(chatMessageReq.getContent());
                break;
            case IMAGE:
                String imageUrl = uploadImage.uploadToCloudinary(chatMessageReq.getFile());
                chatMessage.setImageURL(imageUrl);
                break;
        }

//        if (chatMessageReq.getFile() != null) {
//            String imageUrl = uploadImage.uploadToCloudinary(chatMessageReq.getFile());
//            chatMessage.setImageURL(imageUrl);
//        }
//        if (chatMessageReq.getContent() != null) {
//            chatMessage.setContent(chatMessageReq.getContent());
//        }

        chatMessage.setId(UUID.randomUUID());
        chatMessage.setUserId(chatMessageReq.getUserId());
        chatMessage.setChatRoomId(chatMessageReq.getChatRoomId());
        chatMessage.setCreatedAt(new Date());
        chatMessage.setDisplayName(chatMessageReq.getDisplayName());

        return chatMessageRepo.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessageByRoomId(UUID roomId) {
        return chatMessageRepo.findChatMessageByChatRoomId(roomId);
    }

}
