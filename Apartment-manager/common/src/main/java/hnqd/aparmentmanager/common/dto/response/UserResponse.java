package hnqd.aparmentmanager.common.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String roleName;
    private String status;

}
