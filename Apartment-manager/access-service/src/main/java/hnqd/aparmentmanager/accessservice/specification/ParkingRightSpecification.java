package hnqd.aparmentmanager.accessservice.specification;

import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ParkingRightSpecification {

    public static Specification<ParkingRight> hasRelativeInContractId(List<Integer> contractIds) {
        return (Root<ParkingRight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<ParkingRight, Relative> relativeJoin = root.join("relative");
            Predicate contractPredicate = relativeJoin.get("contractId").in(contractIds);
            return criteriaBuilder.and(contractPredicate);
        };
    }

    public static Specification<ParkingRight> hasParkingRightStatus(String status) {
        return (Root<ParkingRight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction(); // Không lọc theo status nếu không có tham số
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

}
