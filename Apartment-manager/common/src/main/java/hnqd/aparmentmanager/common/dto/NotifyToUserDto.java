package hnqd.aparmentmanager.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotifyToUserDto {

    private Integer userId;

    private String message;

}
