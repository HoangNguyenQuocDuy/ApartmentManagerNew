package hnqd.aparmentmanager.documentservice.entity;

import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.documentservice.converter.PaymentStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ContractTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime termStartDate;
    private LocalDateTime termEndDate;
    private BigDecimal termAmount;
    private Integer termOrder; //
    @Convert(converter = PaymentStatusConverter.class)
    private EPaymentStatus paymentStatus; // PENDING, PAID, OVERDUE

    @ManyToOne
    @JoinColumn(name = "contractId", nullable = false)
    private Contract contract;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
