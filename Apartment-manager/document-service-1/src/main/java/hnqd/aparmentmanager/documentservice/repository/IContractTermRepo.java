package hnqd.aparmentmanager.documentservice.repository;

import hnqd.aparmentmanager.documentservice.entity.ContractTerm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContractTermRepo extends JpaRepository<ContractTerm, Integer> {
    Page<ContractTerm> findAll(Pageable pageable);

    @Query("select ct.id from ContractTerm ct where ct.contract.userId = :userId")
    List<Integer> findContractTermIdsByUserId(Integer userId);
}
