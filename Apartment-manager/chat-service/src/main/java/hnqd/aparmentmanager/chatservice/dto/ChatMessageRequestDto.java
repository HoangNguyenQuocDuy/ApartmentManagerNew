package hnqd.aparmentmanager.chatservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessageRequestDto {

    private String content;
    private int userId;
    private UUID chatRoomId;

}
