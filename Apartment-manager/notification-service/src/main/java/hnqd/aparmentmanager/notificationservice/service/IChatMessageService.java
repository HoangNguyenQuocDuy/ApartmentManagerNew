package hnqd.aparmentmanager.notificationservice.service;

import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;

public interface IChatMessageService {

    void sendMessageToRoom(ChatMessageRequestDto message, String roomId);

}
