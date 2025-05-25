package hnqd.aparmentmanager.visitorservice.specification;

import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import org.springframework.data.jpa.domain.Specification;

public class VisitorSpecification {

    public static Specification<Visitor> containsIdCardNumber(String idCardNumber) {
        return (root, query, cb) -> {
            if (idCardNumber == null) {
                return cb.conjunction();
            }

            return cb.like(root.get("idCardNumber"), "%" + idCardNumber + "%");
        };
    }

}
