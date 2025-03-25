package hnqd.aparmentmanager.visitorservice.entity;

import hnqd.aparmentmanager.visitorservice.serializable.VisitRequestVisitorId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class VisitRequestVisitor {

    @EmbeddedId
    private VisitRequestVisitorId id;

    @ManyToOne
    @MapsId("visitRequestId")
    @JoinColumn(name = "visitRequestId", nullable = false)
    private VisitRequest visitRequest;

    @ManyToOne
    @MapsId("visitorId")
    @JoinColumn(name = "visitorId", nullable = false)
    private Visitor visitor;

}
