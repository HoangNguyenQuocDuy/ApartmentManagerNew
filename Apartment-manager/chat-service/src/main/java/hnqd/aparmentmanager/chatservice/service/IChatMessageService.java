package hnqd.aparmentmanager.chatservice.service;


import hnqd.aparmentmanager.chatservice.dto.ChatMessageRequestDto;
import hnqd.aparmentmanager.chatservice.model.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface IChatMessageService {

    ChatMessage createChatMessage(ChatMessageRequestDto chatMessage);

    List<ChatMessage> getChatMessageByRoomId(UUID roomId);

}
