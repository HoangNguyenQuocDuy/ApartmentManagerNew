package hnqd.aparmentmanager.visitorservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class VisitRequestVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "visitRequestId", nullable = false)
    private VisitRequest visitRequest;

    @ManyToOne
    @JoinColumn(name = "visitorId", nullable = false)
    private Visitor visitor;

}
