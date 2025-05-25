package hnqd.aparmentmanager.accessservice.specification;

import hnqd.aparmentmanager.accessservice.entity.Relative;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RelativeSpecification {

    public static Specification<Relative> hasContractIds(List<Integer> contractIds) {
        return (root, query, cb) -> root.get("contractId").in(contractIds);
    }

}
