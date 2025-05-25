package hnqd.aparmentmanager.common.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {

    private String status;
    private String message;
    private String URL;

}
