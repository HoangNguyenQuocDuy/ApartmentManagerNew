package hnqd.aparmentmanager.authservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private UserResponse userResponse;
    private String accessToken;
}
