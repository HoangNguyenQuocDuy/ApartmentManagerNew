package hnqd.aparmentmanager.paymentservice.specification;

import hnqd.aparmentmanager.paymentservice.entity.Payment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PaymentSpecification {

    public static Specification<Payment> hasPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.between(root.get("vnp_PayDate"), fromDate, toDate);
        };
    }

    public static Specification<Payment> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    }

}
