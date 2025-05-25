package hnqd.aparmentmanager.chatservice.repository;

import hnqd.aparmentmanager.chatservice.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IChatRoomRepo extends MongoRepository<ChatRoom, UUID> {

    List<ChatRoom> findByUserIdsContaining(Integer userId);

    Optional<ChatRoom> findByUserIds(Set<Integer> userIds);

    Optional<ChatRoom> findByRoomName(String roomName);
}
