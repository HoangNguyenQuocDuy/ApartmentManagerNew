package hnqd.apartmentmanager.roomservice.service.impl;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.common.utils.UploadImage;
import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import hnqd.apartmentmanager.roomservice.entity.Room;
import hnqd.apartmentmanager.roomservice.entity.RoomType;
import hnqd.apartmentmanager.roomservice.repository.IRoomRepo;
import hnqd.apartmentmanager.roomservice.repository.IRoomTypeRepo;
import hnqd.apartmentmanager.roomservice.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {
    private final IRoomRepo roomRepo;
    private final IRoomTypeRepo roomTypeRepo;
    private final UploadImage uploadImage;

    @Override
    public Room createRoom(RoomRequest roomReq) throws IOException {
        if (roomRepo.existsByName(roomReq.getName())) {
            throw new CommonException.DuplicationError("Room with name has exists");
        }

        Room room = new Room();
        if (roomReq.getFile() != null) {
                room.setImage(uploadImage.uploadToCloudinary(roomReq.getFile()));
        }
        room.setStatus("Available");
        room.setName(roomReq.getName());
        room.setRoomType(roomTypeRepo.findById(roomReq.getRoomTypeId())
                .orElseThrow(() -> new CommonException.NotFoundException("Room type not found")));

        return roomRepo.save(room);
    }

    @Override
    public List<Room> getRooms(Map<String, String> params) {
        if (params.get("status") != null && !params.get("status").isEmpty()) {
            return roomRepo.findAllByStatus(params.get("status"));
        } else return roomRepo.findAll();
    }

    @Override
    public Optional<Room> getRoomById(Integer id) {
        return roomRepo.findById(id);
    }

    @Override
    public Page<Room> getRoomsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);
        if (params.get("status") != null && !params.get("status").isEmpty()) {
            return roomRepo.findAllByStatus(pageable, params.get("status"));
        }
        return roomRepo.findAll(pageable);
    }

    @Override
    public Room updateRoom(RoomRequest roomReq, Integer roomId) {
        boolean flag = false;

        Room room = roomRepo.findById(roomId).orElseThrow(
                () -> (new CommonException.NotFoundException("Room not found!"))
        );

        if (roomReq.getName() != null && !roomReq.getName().isEmpty()) {
            room.setName(roomReq.getName());
            flag = true;
        }
        if (roomReq.getRoomTypeId() != room.getRoomType().getId()) {
            RoomType roomType = roomTypeRepo.findById(roomReq.getRoomTypeId()).orElseThrow(
                    () -> (new CommonException.NotFoundException("Room type from request not found!"))
            );
            room.setRoomType(roomType);
            flag = true;
        }
//        if (roomReq.getFile() != null && !roomReq.getFile().isEmpty() && !roomReq.getFile().equals(room.getImage())) {
//            room.setImage(uploadImage.uploadImageToCloudinary(roomReq.getFile()));
//            flag = true;
//        }

        return roomRepo.save(room);
    }

    @Override
    public void deleteRoom(Integer id) {
        roomRepo.deleteById(id);
    }
}
