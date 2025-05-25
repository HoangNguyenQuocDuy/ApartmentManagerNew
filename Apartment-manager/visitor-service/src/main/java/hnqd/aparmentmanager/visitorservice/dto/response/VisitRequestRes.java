package hnqd.aparmentmanager.visitorservice.dto.response;

import hnqd.aparmentmanager.common.Enum.EVisitStatus;
import hnqd.aparmentmanager.visitorservice.entity.SecurityLog;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class VisitRequestRes {

    private Integer id;

    private Integer residentId;

    private Integer roomId;

    private LocalDateTime visitDate;

    private LocalDateTime expectedCheckinTime;

    private EVisitStatus status = EVisitStatus.PENDING;

    private Set<Visitor> visitors = new HashSet<>();

    private Set<SecurityLog> securityLogs = new HashSet<>();

}
