package hnqd.apartmentmanager.roomservice.repository;

import hnqd.apartmentmanager.roomservice.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoomTypeRepo extends JpaRepository<RoomType, Integer> {
}
