package hnqd.aparmentmanager.visitorservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VisitorRequestDto {

    @NotBlank(message = "FullName is not allow to blank")
    private String fullname;
    @NotBlank(message = "Phone is not allow to blank")
    private String phone;
    @NotBlank(message = "Id card number is not allow to blank")
    private String idCardNumber;

}
