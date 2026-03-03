package hnqd.aparmentmanager.roomservice.repository;

import hnqd.aparmentmanager.roomservice.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoomTypeRepo extends JpaRepository<RoomType, Integer> {
}
