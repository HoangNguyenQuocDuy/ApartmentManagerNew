package hnqd.aparmentmanager.visitorservice.service.impl;

import hnqd.aparmentmanager.common.Enum.EVisitStatus;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.visitorservice.dto.response.VisitRequestVisitorRes;
import hnqd.aparmentmanager.visitorservice.entity.SecurityLog;
import hnqd.aparmentmanager.visitorservice.entity.VisitRequestVisitor;
import hnqd.aparmentmanager.visitorservice.repository.ISecurityLogRepository;
import hnqd.aparmentmanager.visitorservice.repository.IVisitRequestVisitorRepo;
import hnqd.aparmentmanager.visitorservice.service.IVisitRequestVisitorService;
import hnqd.aparmentmanager.visitorservice.specification.VisitRequestVisitorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitRequestVisitorImpl implements IVisitRequestVisitorService {

    private final IVisitRequestVisitorRepo visitRequestVisitorRepo;
    private final ISecurityLogRepository securityLogRepository;

    @Override
    public RestResponse<ListResponse<VisitRequestVisitorRes>> getListVisitRequestVisitor(Map<String, String> filterMap) {
        int page = Integer.parseInt(filterMap.get("page"));
        int size = Integer.parseInt(filterMap.get("size"));
        int roomId = Integer.parseInt(filterMap.getOrDefault("roomId", "0"));
        boolean all = Boolean.parseBoolean(filterMap.getOrDefault("all", "false"));

        Specification<VisitRequestVisitor> specification = Specification.where(null);

        // Ví dụ: parse filter từ JSON hoặc Map<String, String>
        if (filterMap.containsKey("visitDate")) {
            LocalDateTime visitDate = LocalDateTime.parse(filterMap.get("visitDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            specification = specification.and(VisitRequestVisitorSpecification.hasVisitDate(visitDate));
        }

        if (filterMap.containsKey("fromDate")) {
            LocalDateTime fromDate = LocalDateTime.parse(filterMap.get("fromDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime toDate = filterMap.containsKey("toDate")
                    ? LocalDateTime.parse(filterMap.get("toDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : LocalDateTime.now();

            specification = specification.and(VisitRequestVisitorSpecification.fromDateToDate(fromDate, toDate));
        }

        if (filterMap.containsKey("status")) {
            EVisitStatus status = EVisitStatus.safeValueOfName(filterMap.get("status").toUpperCase());
            specification = specification.and(VisitRequestVisitorSpecification.hasStatus(status));
        }
        if (filterMap.containsKey("visitorName")) {
            specification = specification.and(VisitRequestVisitorSpecification.hasVisitorName(filterMap.get("visitorName")));
        }
        if (roomId != 0) {
            specification = specification.and(VisitRequestVisitorSpecification.hasRoomId(roomId));
        }
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<VisitRequestVisitor> pageResult = visitRequestVisitorRepo.findAll(specification, pageable);

        List<VisitRequestVisitorRes> result = pageResult.getContent().stream()
                .map(this::mapToRes)
                .toList();

        return RestResponse.ok(ListResponse.of(result));
    }

    private VisitRequestVisitorRes mapToRes(VisitRequestVisitor entity) {
        VisitRequestVisitorRes res = new VisitRequestVisitorRes();
        res.setVisitRequest(entity.getVisitRequest());
        res.setVisitor(entity.getVisitor());

        Optional<SecurityLog> securityLog =
                securityLogRepository.findByVisitorIdAndVisitRequestId(entity.getVisitor().getId(), entity.getVisitRequest().getId());

        if (securityLog.isPresent()) {
            res.setCheckinTime(securityLog.get().getCheckinTime());
            res.setCheckoutTime(securityLog.get().getCheckoutTime());
        }

        return res;
    }

}
