package hnqd.aparmentmanager.documentservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContractTermDto {

    private Integer contractId;
    private LocalDateTime termStartDate;
    private LocalDateTime termEndDate;
    private BigDecimal termAmount;

}
