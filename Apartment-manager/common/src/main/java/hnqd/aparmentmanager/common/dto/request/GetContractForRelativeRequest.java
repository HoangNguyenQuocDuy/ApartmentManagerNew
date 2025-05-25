package hnqd.aparmentmanager.common.dto.request;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetContractForRelativeRequest {
    
    private Integer userId;
    private Integer roomId;
    private EContractStatus status;
    
}
