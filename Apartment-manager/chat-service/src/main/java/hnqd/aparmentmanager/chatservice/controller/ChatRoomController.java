package hnqd.aparmentmanager.chatservice.controller;

import hnqd.aparmentmanager.chatservice.dto.ChatMessageRequestDto;
import hnqd.aparmentmanager.chatservice.model.ChatMessage;
import hnqd.aparmentmanager.chatservice.model.ChatRoom;
import hnqd.aparmentmanager.chatservice.service.IChatMessageService;
import hnqd.aparmentmanager.chatservice.service.IChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final IChatRoomService chatRoomService;
    private final IChatMessageService chatMessageService;
    private final RabbitTemplate rabbitTemplate;
//    private final IUserService userService;

//    @MessageMapping("/sendMessage")
//    public ChatMessage sendMessage(@Payload ChatMessageRequestDto chatMessageReq) {
//        ChatMessage chatMessageSaved = chatMessageService.createChatMessage(chatMessageReq);
//        ChatRoom chatRoom = chatRoomService.getRoomById(chatMessageSaved.getChatRoomId());
//        chatRoom.setUpdatedAt(new Date());
//        chatRoom.setLastMessage(chatMessageSaved.getContent());
//        chatRoomService.updateRoom(chatRoom);
//
//        rabbitTemplate.convertAndSend("chatExchange", "G8wMk8fKtQ", chatMessageReq);
//
//        return chatMessageSaved;
//    }

//    @MessageMapping("/createRoom")
//    public ChatRoom addRoom(@Payload ChatRoomRequestDto chatRoomRequestDto) {
//        try {
//            ChatRoom chatRoom = chatRoomService.createRoom(chatRoomRequestDto);
//            messagingTemplate.convertAndSend("/rooms", chatRoom);
//
//            return chatRoom;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @MessageMapping("/chat.addUserToChatRoom")
//    public ChatRoom addUserToChatRoom(@Payload Map<String, Object> payload) {
//        try {
//            UUID roomId = UUID.fromString((String) payload.get("roomId"));
//            List<String> userIds = (List<String>) payload.get("usersIds");
//
//            ChatRoom chatRoom = chatRoomService.addUserToRoom(roomId, userIds);
//            messagingTemplate.convertAndSend(String.format("/rooms/%s/addUser", roomId), chatRoom);
//
//            return chatRoom;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @MessageMapping("/chat.deleteUserFromRoom")
//    public ChatRoom deleteUserFromRoom(@Payload Map<String, Object> payload) {
//        try {
//            UUID roomId = UUID.fromString((String) payload.get("roomId"));
//            UUID userId = UUID.fromString((String) payload.get("userId"));
//
//            ChatRoom chatRoom = chatRoomService.deleteUserFromRoom(roomId, userId);
//            messagingTemplate.convertAndSend(String.format("/rooms/%s/deleteUser", roomId), chatRoom);
//
//            return chatRoom;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @GetMapping("/{roomId}")
//    public ResponseEntity<ResponseObject> getRoom(@PathVariable UUID roomId) {
//        try {
//            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
//                    new ResponseObject("OK", "Get room by id successfully", chatRoomService.getRoomById(roomId))
//            );
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
//                    new ResponseObject("FAILED", "Failed when get room by id!", e.getMessage())
//            );
//        }
//    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getChatRooms(@RequestHeader("X-User-Id") Integer userId) {
        List<ChatRoom> chatRooms = chatRoomService.findRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }
}
