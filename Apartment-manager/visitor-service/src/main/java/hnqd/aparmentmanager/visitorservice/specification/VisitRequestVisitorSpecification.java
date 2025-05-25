package hnqd.aparmentmanager.visitorservice.specification;

import hnqd.aparmentmanager.common.Enum.EVisitStatus;
import hnqd.aparmentmanager.visitorservice.entity.VisitRequest;
import hnqd.aparmentmanager.visitorservice.entity.VisitRequestVisitor;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class VisitRequestVisitorSpecification {

    public static Specification<VisitRequestVisitor> hasVisitDate(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) return cb.conjunction();

            Join<VisitRequestVisitor, VisitRequest> visitRequestJoin = root.join("visitRequest");
            LocalDateTime start = date.toLocalDate().atStartOfDay();
            LocalDateTime end = date.toLocalDate().atTime(LocalTime.MAX);
            return cb.between(visitRequestJoin.get("visitDate"), start, end);
        };
    }

    public static Specification<VisitRequestVisitor> fromDateToDate(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null || to == null) return cb.conjunction();

            Join<VisitRequestVisitor, VisitRequest> visitRequestJoin = root.join("visitRequest");
            return cb.between(visitRequestJoin.get("visitDate"), from, to);
        };
    }

    public static Specification<VisitRequestVisitor> hasStatus(EVisitStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();

            Join<VisitRequestVisitor, VisitRequest> visitRequestJoin = root.join("visitRequest");
            return cb.equal(visitRequestJoin.get("status"), status);
        };
    }

    public static Specification<VisitRequestVisitor> hasVisitorName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return cb.conjunction();

            Join<VisitRequestVisitor, Visitor> visitorJoin = root.join("visitor");
            return cb.like(cb.lower(visitorJoin.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<VisitRequestVisitor> hasVisitorPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isEmpty()) return cb.conjunction();

            Join<VisitRequestVisitor, Visitor> visitorJoin = root.join("visitor");
            return cb.like(visitorJoin.get("phone"), "%" + phone + "%");
        };
    }

    public static Specification<VisitRequestVisitor> hasRoomId(Integer roomId) {
        return (root, query, cb) -> {
            if (roomId == null) return cb.conjunction();

            Join<VisitRequestVisitor, VisitRequest> visitRequestJoin = root.join("visitRequest");
            return cb.equal(visitRequestJoin.get("roomId"), roomId);
        };
    }

}
