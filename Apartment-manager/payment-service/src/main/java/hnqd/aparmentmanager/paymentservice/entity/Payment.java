package hnqd.aparmentmanager.paymentservice.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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

//    private String vnp_TmnCode;
//    private Long vnp_Amount;
//    private String vnp_BankCode;
//    private String vnp_BankTranNo;
//    private Long vnp_CardType;
//    private Long vnp_PayDate;
//    //    private String vnp_OrderInfo;
//    private Long vnp_TransactionNo;
//    private Long vnp_ResponseCode;
//    private Long vnp_TransactionStatus;
//    private String vnp_TxnRef;
//    private String vnp_SecureHash;

}
