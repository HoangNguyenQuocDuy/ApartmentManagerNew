package hnqd.apartmentmanager.roomservice.service;

import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import hnqd.apartmentmanager.roomservice.entity.Room;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRoomService {

    Room createRoom(RoomRequest room) throws IOException;

    List<Room> getRooms(Map<String, String> params);

    Room getRoomById(Integer id);

    Page<Room> getRoomsPaging(Map<String, String> params);

    Room updateRoom(RoomRequest roomReq, Integer roomId);

    void deleteRoom(Integer id);

    Page<Integer> getRoomIdsByUserId(Integer userId, int page, int size);

}
