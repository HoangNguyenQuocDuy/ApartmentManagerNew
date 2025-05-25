package hnqd.aparmentmanager.accessservice.repository;

import hnqd.aparmentmanager.accessservice.entity.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IRelativeRepository extends JpaRepository<Relative, Integer>, JpaSpecificationExecutor<Relative> {

    Optional<Relative> findByIdCard(String idCard);

}
