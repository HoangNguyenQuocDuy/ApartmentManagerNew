package hnqd.aparmentmanager.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Integer invoiceId;
    private Integer userId;

}
