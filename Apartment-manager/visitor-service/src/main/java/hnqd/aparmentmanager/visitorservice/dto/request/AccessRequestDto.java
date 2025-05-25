package hnqd.aparmentmanager.visitorservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRequestDto {

    private Integer staffId;
    private String idCardNumber;
    private Integer visitRequestId;
    private Integer securityLogId;

}
