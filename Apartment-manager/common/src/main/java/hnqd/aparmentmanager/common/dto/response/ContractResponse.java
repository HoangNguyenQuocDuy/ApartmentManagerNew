package hnqd.aparmentmanager.common.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ContractResponse {

    private Integer id;
    private String contractNumber;
    private String contractType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer userId; // FK to user
    private Integer roomId; // FK to room
    private BigDecimal totalAmount;
    private String termFrequency; // Contract term (Monthly, Quarterly, ...)
    private String status;
    private long totalTerms;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
