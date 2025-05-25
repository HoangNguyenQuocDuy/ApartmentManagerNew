package hnqd.aparmentmanager.visitorservice.specification;

import hnqd.aparmentmanager.common.Enum.ESecurityType;
import hnqd.aparmentmanager.visitorservice.entity.SecurityLog;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class SecurityLogSpecification {

    public static Specification<SecurityLog> hasStatus(ESecurityType status) {
        return ((root, query, cb) -> {
           if (status == null) {
               return cb.conjunction();
           }

           return cb.equal(root.get("status"), status);
        });
    }

    public static Specification<SecurityLog> hasVisitorIdCardNumber(String idCardNumber) {
        return ((root, query, cb) -> {
            if (idCardNumber == null) {
                return cb.conjunction();
            }

            Join<SecurityLog, Visitor> visitorJoin = root.join("visitor");

            return cb.equal(visitorJoin.get("idCardNumber"), idCardNumber);
        });
    }

}
