package hnqd.aparmentmanager.common.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
public class ChatMessageRequestDto {

    private String content;
    private int userId;
    private UUID chatRoomId;
    private MultipartFile file;
    private String type;
    private String displayName;

}
