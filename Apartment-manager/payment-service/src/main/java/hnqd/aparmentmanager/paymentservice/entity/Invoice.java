package hnqd.aparmentmanager.paymentservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.serialize.EnumSerialize;
import hnqd.aparmentmanager.common.serialize.LocalDateTimeSerialize;
import hnqd.aparmentmanager.paymentservice.converter.InvoiceTypeConverter;
import hnqd.aparmentmanager.paymentservice.converter.PaymentStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal amount;
    @JsonSerialize(using = LocalDateTimeSerialize.class)
    private LocalDateTime dueDate;
    private String description;
    @JsonSerialize(using = EnumSerialize.class)
    @Convert(converter = PaymentStatusConverter.class)
    private EPaymentStatus invoiceStatus;
    @JsonSerialize(using = EnumSerialize.class)
    @Convert(converter = InvoiceTypeConverter.class)
    private EInvoiceType invoiceType;

    private LocalDateTime paidAt;

    private Integer contractTermId; // FK to contractTerm
    private Integer roomId; // FK to room

//    @JsonIgnore
//    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
//    private Set<Payment> payments;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
