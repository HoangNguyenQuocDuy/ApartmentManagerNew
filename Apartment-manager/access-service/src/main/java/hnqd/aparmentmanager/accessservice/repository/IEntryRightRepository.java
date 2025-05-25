package hnqd.aparmentmanager.accessservice.repository;

import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntryRightRepository extends JpaRepository<EntryRight, Integer>, JpaSpecificationExecutor<EntryRight> {
}
