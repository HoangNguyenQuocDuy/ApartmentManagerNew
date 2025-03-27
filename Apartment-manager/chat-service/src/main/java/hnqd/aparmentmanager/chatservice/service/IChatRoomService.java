package hnqd.aparmentmanager.chatservice.service;

import hnqd.aparmentmanager.chatservice.dto.ChatRoomRequestDto;
import hnqd.aparmentmanager.chatservice.model.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface IChatRoomService {

    ChatRoom createRoom(ChatRoomRequestDto chatRoomBody);

    ChatRoom getRoomById(UUID roomId);

    List <ChatRoom> findRoomsByUserId(int userId);

    void deleteRoom(UUID roomId);

    ChatRoom addUserToRoom(UUID roomId, List<String> userId);

    ChatRoom updateRoom(ChatRoom chatRoom);

}
