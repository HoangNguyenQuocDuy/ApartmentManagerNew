package hnqd.aparmentmanager.common.dto.request;

import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InvoiceReqDto {

    private BigDecimal amount;
    @Nullable
    private LocalDateTime dueDate;
    private EInvoiceType invoiceType;
    private String description;
    private Integer contractTermId; //FK to contractTerm
    private Integer roomId;
    @Nullable
    private String dueDateLong;

}
