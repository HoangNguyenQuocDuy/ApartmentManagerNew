package hnqd.aparmentmanager.visitorservice.specification;

import hnqd.aparmentmanager.visitorservice.entity.VisitRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class VisitRequestSpecification {

    public static Specification<VisitRequest> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<VisitRequest> hasDay(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime start = date.toLocalDate().atStartOfDay();
            LocalDateTime end = date.toLocalDate().atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("visitDate"), start, end);
        };
    }

    public static Specification<VisitRequest> hasUserId(int userId) {
        return (root, query, criteriaBuilder) -> {
          if (userId == 0) {
              return criteriaBuilder.conjunction();
          }

          return criteriaBuilder.equal(root.get("residentId"), userId);
        };
    }

    public static Specification<VisitRequest> hasRoomId(int roomId) {
        return (root, query, criteriaBuilder) -> {
            if (roomId == 0) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("roomId"), roomId);
        };
    }

}
