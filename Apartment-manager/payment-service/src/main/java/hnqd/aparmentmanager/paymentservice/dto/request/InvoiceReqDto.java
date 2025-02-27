package hnqd.aparmentmanager.paymentservice.dto.request;

import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceReqDto {

    private BigDecimal amount;
    private LocalDateTime dueDate;
    private EInvoiceType invoiceType;
    private String description;
    private Integer contractTermId; //FK to contractTerm
    private Integer roomId;

}
