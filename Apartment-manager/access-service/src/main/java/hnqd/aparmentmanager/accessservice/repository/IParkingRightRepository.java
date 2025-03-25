package hnqd.aparmentmanager.accessservice.repository;

import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IParkingRightRepository extends JpaRepository<ParkingRight, Integer>, JpaSpecificationExecutor<ParkingRight> {
}
