package hnqd.aparmentmanager.accessservice.repository;

import hnqd.aparmentmanager.accessservice.entity.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IRelativeRepository extends JpaRepository<Relative, Integer>, JpaSpecificationExecutor<Relative> {
}
