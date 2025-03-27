package hnqd.aparmentmanager.chatservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private UUID id;
    private String content;
    private int userId;
    private UUID chatRoomId;
    private Date createdAt;
    private Date updatedAt;
}
