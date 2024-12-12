package hnqd.apartmentmanager.roomservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class RoomRequest {
    private String name;
    private Integer roomTypeId;
    private MultipartFile file;
}
