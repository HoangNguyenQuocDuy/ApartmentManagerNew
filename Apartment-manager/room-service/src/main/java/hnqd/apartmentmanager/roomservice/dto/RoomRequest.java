package hnqd.apartmentmanager.roomservice.dto;

import hnqd.aparmentmanager.common.Enum.ERoomStatus;
import jakarta.validation.constraints.Null;
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
