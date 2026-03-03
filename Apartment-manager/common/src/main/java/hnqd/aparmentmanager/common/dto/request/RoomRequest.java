package hnqd.aparmentmanager.common.dto.request;

import hnqd.aparmentmanager.common.enums.ERoomStatus;
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
    private ERoomStatus roomStatus;

}
