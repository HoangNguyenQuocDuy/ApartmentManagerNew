package hnqd.aparmentmanager.accessservice.specification;

import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class EntryRightSpecification {

    public static Specification<EntryRight> hasRelativeInContractId(List<Integer> contractIds) {
        return (Root<EntryRight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (contractIds.isEmpty()) return criteriaBuilder.disjunction();

            Join<EntryRight, Relative> relativeJoin = root.join("relative");
            Predicate contractPredicate = relativeJoin.get("contractId").in(contractIds);
            return criteriaBuilder.and(contractPredicate);
        };
    }

    public static Specification<EntryRight> hasEntryRightStatus(String status) {
        return (Root<EntryRight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction(); // Không lọc theo status nếu không có tham số
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

}
