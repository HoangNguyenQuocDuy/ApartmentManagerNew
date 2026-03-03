package hnqd.aparmentmanager.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class UserRequest {
    private String username;
    private String firstname;
    private String lastname;
    @NotBlank(message = "password can not be blank or empty")
    private String password;
    private String phone;
    private String email;
    private String status;
    @NotBlank(message = "avatar can not be blank or empty")
    private MultipartFile file;
}
