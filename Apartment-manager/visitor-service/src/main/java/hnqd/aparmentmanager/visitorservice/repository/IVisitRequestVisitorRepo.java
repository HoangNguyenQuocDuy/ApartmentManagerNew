package hnqd.aparmentmanager.visitorservice.repository;

import hnqd.aparmentmanager.visitorservice.entity.VisitRequestVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IVisitRequestVisitorRepo extends JpaRepository<VisitRequestVisitor, Integer>,
        JpaSpecificationExecutor<VisitRequestVisitor> {
}
