package hnqd.apartmentmanager.roomservice.repository;

import hnqd.apartmentmanager.roomservice.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomRepo extends JpaRepository<Room, Integer> {
    List<Room> findAllByStatus(String status);

    Page<Room> findAll(Pageable pageable);

    Page<Room> findAllByStatus(Pageable pageable, String status);

    Boolean existsByName(String name);
}
