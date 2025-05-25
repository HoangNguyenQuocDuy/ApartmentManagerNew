package hnqd.aparmentmanager.visitorservice.dto.response;

import hnqd.aparmentmanager.visitorservice.entity.VisitRequest;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VisitRequestVisitorRes {

    private VisitRequest visitRequest;

    private Visitor visitor;

    private LocalDateTime checkinTime;

    private LocalDateTime checkoutTime;

}
