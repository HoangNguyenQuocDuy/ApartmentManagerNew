package hnqd.aparmentmanager.paymentservice.dto.request;

import hnqd.aparmentmanager.common.enums.EPaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentUpdateDto {

    private EPaymentStatus status;

}
