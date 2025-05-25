package hnqd.aparmentmanager.chatservice.service;


import hnqd.aparmentmanager.chatservice.model.ChatMessage;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IChatMessageService {

    ChatMessage createChatMessage(ChatMessageRequestDto chatMessage) throws IOException;

    List<ChatMessage> getChatMessageByRoomId(UUID roomId);

}
