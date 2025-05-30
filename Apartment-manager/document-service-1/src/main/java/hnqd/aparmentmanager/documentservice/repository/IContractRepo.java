package hnqd.aparmentmanager.documentservice.repository;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IContractRepo extends JpaRepository<Contract, Integer>, JpaSpecificationExecutor<Contract> {

    Page<Contract> findAll(Pageable pageable);

    Page<Contract> findAllByUserId(Integer userId, Pageable pageable);

    @Query("select c.roomId from Contract c where c.userId = :userId AND c.endDate >= CURRENT_TIMESTAMP")
    List<Integer> getRoomIdsByUserId(Integer userId);

    @Query("select c.userId from Contract c where c.roomId = :roomId AND c.endDate >= CURRENT_TIMESTAMP")
    Integer getUserIdByRoomId(Integer roomId);

    Contract findByUserIdAndRoomIdAndStatus(Integer userId, Integer roomId, EContractStatus status);

    @Query("select c.id from Contract c where c.userId =:userId and c.status =:status")
    List<Integer> findContractIdsByUserIdAndStatus(Integer userId, EContractStatus status);

    List<Integer> findAllByRoomIdAndStatus(Integer roomId, EContractStatus status);

    List<Integer> findAllByEndDateAfter(LocalDateTime time);

    List<Contract> findAllByStatus(EContractStatus status);

}
