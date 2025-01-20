package hnqd.aparmentmanager.lockerservice.repository;


import hnqd.project.ApartmentManagement.entity.Locker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILockerRepo extends JpaRepository<Locker, Integer> {
    List<Locker> findAllByStatus(String status);
    Page<Locker> findAll(Pageable pageable);
}
