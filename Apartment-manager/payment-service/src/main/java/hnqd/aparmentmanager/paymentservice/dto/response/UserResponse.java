package hnqd.aparmentmanager.paymentservice.dto.response;

import lombok.*;

import java.util.Map;

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
