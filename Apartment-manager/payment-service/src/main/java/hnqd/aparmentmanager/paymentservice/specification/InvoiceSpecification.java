package hnqd.aparmentmanager.paymentservice.specification;

import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceSpecification {

    public static Specification<Invoice> hasInvoiceStatus(String status) {
        return (Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction(); // Không lọc theo status nếu không có tham số
            }
            return criteriaBuilder.equal(root.get("invoiceStatus"), status);
        };
    }

    public static Specification<Invoice> hasInvoiceType(String type) {
        return (Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (type == null) {
                return criteriaBuilder.conjunction(); // Không lọc theo type nếu không có tham số
            }
            return criteriaBuilder.equal(root.get("invoiceType"), type);
        };
    }

    public static Specification<Invoice> hasCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction(); // Không lọc nếu không có tham số
            }

            LocalDateTime startOfDay = fromDate != null ? fromDate.withHour(0).withMinute(0).withSecond(0).withNano(0) : null;
            LocalDateTime endOfDay = toDate != null ? toDate.withHour(23).withMinute(59).withSecond(59).withNano(999999999) : null;

            return criteriaBuilder.between(root.get("createdAt"), startOfDay, endOfDay);
        };
    }

    public static Specification<Invoice> hasContractTermIdIn(List<Integer> contractTermIds) {
        return (root, query, criteriaBuilder) -> {
            if (contractTermIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(
                    root.get("contractTermId").in(contractTermIds),
                    criteriaBuilder.isNull(root.get("contractTermId"))
            );
        };
    }

    public static Specification<Invoice> hasRoomIdsIn(List<Integer> roomIds) {
        return (root, query, criteriaBuilder) -> {
            if (roomIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.in(root.get("roomId")).value(roomIds);
        };
    }
}
