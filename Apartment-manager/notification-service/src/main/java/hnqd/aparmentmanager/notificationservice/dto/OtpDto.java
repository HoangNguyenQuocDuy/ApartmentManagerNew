package hnqd.aparmentmanager.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpDto {
    private String otp;
    private String userId;
    private Long expiredTime;
}
