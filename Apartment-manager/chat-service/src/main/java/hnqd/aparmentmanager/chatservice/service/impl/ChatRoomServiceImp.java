package hnqd.aparmentmanager.chatservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.chatservice.client.IAuthServiceClient;
import hnqd.aparmentmanager.chatservice.dto.ChatRoomRequestDto;
import hnqd.aparmentmanager.chatservice.model.ChatRoom;
import hnqd.aparmentmanager.chatservice.repository.IChatRoomRepo;
import hnqd.aparmentmanager.chatservice.service.IChatRoomService;
import hnqd.aparmentmanager.common.dto.response.UserResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImp implements IChatRoomService {
    private final IAuthServiceClient authServiceClient;
    private final IChatRoomRepo chatRoomRepo;
    private final ObjectMapper objectMapper;

    @Override
    public ChatRoom createRoom(ChatRoomRequestDto chatRoomBody) {
        Set<Integer> userIds = new HashSet<>();

        chatRoomBody.getUsersIds().forEach(userId -> {
            UserResponse user = objectMapper.convertValue(authServiceClient.getUserById(userId).getBody().getData(), UserResponse.class);
            userIds.add(user.getId());
        });

        ChatRoom newChatRoom = ChatRoom.builder()
                .id(UUID.randomUUID())
                .userIds(userIds)
                .createdAt(new Date())
                .build();

        return chatRoomRepo.save(newChatRoom);
    }

    @Override
    public ChatRoom getRoomById(UUID roomId) {
        ChatRoom chatRoom = chatRoomRepo.findById(roomId).orElseThrow(
                () -> new CommonException.NotFoundException("Room with id " + roomId + " not found!")
        );
        ;
        return chatRoom;
    }

    @Override
    public List<ChatRoom> findRoomsByUserId(int userId) {
        UserResponse user = objectMapper.convertValue(authServiceClient.getUserById(userId).getBody().getData(), UserResponse.class);

        return chatRoomRepo.findByUserIdsContaining(user.getId());
    }

    @Override
    public void deleteRoom(UUID roomId) {

    }

    @Override
    public ChatRoom addUserToRoom(UUID roomId, List<String> userId) {
        return null;
    }

    @Override
    public ChatRoom updateRoom(ChatRoom chatRoom) {
        return null;
    }

    @Override
    public ChatRoom findOrCreateRoomWithAdmin(int userId, int adminId) {
        Set<Integer> userIds = new HashSet<>(Set.of(userId, adminId));

        Optional<ChatRoom> existingRoom = chatRoomRepo.findByUserIds(userIds);

        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChatRoom room = ChatRoom.builder()
                .id(UUID.randomUUID())
                .userIds(userIds)
                .roomName("User-" + userId + "-Admin")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return chatRoomRepo.save(room);
    }

    @Override
    public void addUserToRoom(Integer userId, String roomName) {
        ChatRoom existingRoom = chatRoomRepo.findByRoomName(roomName)
                .orElseThrow(() -> new CommonException.NotFoundException("Room with name " + roomName + " not found!"));

        existingRoom.addUser(userId);

        chatRoomRepo.save(existingRoom);
    }
}
