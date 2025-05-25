package hnqd.aparmentmanager.chatservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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
    private String type;
    private String imageURL;
    private String displayName;

    @CreatedDate
    @Indexed
    private Date createdAt;

    @LastModifiedDate
    @Indexed
    private Date updatedAt;

}
