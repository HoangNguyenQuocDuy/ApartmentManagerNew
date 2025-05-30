package hnqd.aparmentmanager.documentservice.dto;

import hnqd.aparmentmanager.common.Enum.EContractType;
import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContractDto {

    private BigDecimal totalAmount;
    private ETermFrequency termFrequency;
    private Integer customerId;
    private Integer roomId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EContractType contractType;
    private Long totalTerms;

}
