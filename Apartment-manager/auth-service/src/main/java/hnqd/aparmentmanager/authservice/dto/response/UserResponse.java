package hnqd.aparmentmanager.authservice.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String roleName;
    private String status;
    private String avatar;

}
