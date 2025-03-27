package hnqd.aparmentmanager.chatservice.repository;

import hnqd.aparmentmanager.chatservice.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface IChatRoomRepo extends MongoRepository<ChatRoom, UUID> {

    List<ChatRoom> findByUserIdsContaining(Integer userId);

}
