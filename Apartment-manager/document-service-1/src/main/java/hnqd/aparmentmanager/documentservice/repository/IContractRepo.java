package hnqd.aparmentmanager.documentservice.repository;

import hnqd.aparmentmanager.documentservice.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContractRepo extends JpaRepository<Contract, Integer> {

    Page<Contract> findAll(Pageable pageable);

    Page<Contract> findAllByUserId(Integer userId, Pageable pageable);

    @Query("select c.roomId from Contract c where c.userId = :userId")
    List<Integer> getRoomIdsByUserId(Integer userId);

}
