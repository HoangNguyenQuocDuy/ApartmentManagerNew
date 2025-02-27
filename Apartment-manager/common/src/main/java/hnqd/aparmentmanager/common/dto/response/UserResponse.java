package hnqd.aparmentmanager.common.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String roleName;
    private String status;

}
