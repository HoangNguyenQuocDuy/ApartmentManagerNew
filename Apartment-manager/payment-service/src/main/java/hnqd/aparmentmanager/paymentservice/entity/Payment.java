package hnqd.aparmentmanager.paymentservice.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "payment")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String transactionId;
    private String provider; // "VNPAY", "MOMO", "PAYPAL"
    private Double amount;
    private String status;
    private Integer userId;
    private Integer invoiceId;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Map<String, Object> extraData;
}
