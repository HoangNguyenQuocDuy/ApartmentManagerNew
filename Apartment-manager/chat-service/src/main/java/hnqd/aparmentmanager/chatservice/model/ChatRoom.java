package hnqd.aparmentmanager.chatservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class ChatRoom implements Comparable<ChatRoom> {
    @Id
    private UUID id;
    private Set<Integer> userIds;
    private String lastMessage;
    private String roomName;
    @CreatedDate
    @Indexed
    private Date createdAt;

    @LastModifiedDate
    @Indexed
    private Date updatedAt;

    @Override
    public int compareTo(ChatRoom otherChatRoom) {
        Date updatedDate1 = this.getUpdatedAt();
        Date updatedDate2 = otherChatRoom.getUpdatedAt();

        if (updatedDate1 != null && updatedDate2 != null) {
            return updatedDate2.compareTo(updatedDate1);
        } else if (updatedDate1 == null && updatedDate2 == null) {
            return 1;
        } else if (updatedDate1 != null) {
            return -1;
        } else {
            return otherChatRoom.getCreatedAt().compareTo(this.getCreatedAt());
        }
    }

    public void addUser(Integer userId) {
        userIds.add(userId);
    }
}
