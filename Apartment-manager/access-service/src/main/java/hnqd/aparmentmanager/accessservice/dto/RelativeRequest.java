package hnqd.aparmentmanager.accessservice.dto;

import hnqd.aparmentmanager.common.Enum.ERelativeType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RelativeRequest {

    private MultipartFile file;
    private String fullName;
    private ERelativeType relationship;
    private String idCard;
    private Integer roomId;
    private Integer userId;

}
