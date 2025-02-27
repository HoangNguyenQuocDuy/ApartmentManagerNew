package hnqd.apartmentmanager.roomservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.dto.response.UserResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.common.utils.UploadImage;
import hnqd.apartmentmanager.roomservice.client.IDocumentServiceClient;
import hnqd.apartmentmanager.roomservice.client.IUserServiceClient;
import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import hnqd.apartmentmanager.roomservice.entity.Room;
import hnqd.apartmentmanager.roomservice.entity.RoomType;
import hnqd.apartmentmanager.roomservice.repository.IRoomRepo;
import hnqd.apartmentmanager.roomservice.repository.IRoomTypeRepo;
import hnqd.apartmentmanager.roomservice.service.IRoomService;
import hnqd.apartmentmanager.roomservice.specifiaction.RoomSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final IUserServiceClient userServiceClient;
    private final IDocumentServiceClient documentServiceClient;
    private final ObjectMapper objectMapper;

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
    public Room getRoomById(Integer id) {
        return roomRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("Room with id " + id + " not found")
        );
    }

    @Override
    public Page<Room> getRoomsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        String status = params.getOrDefault("status", "");
        int userId = Integer.valueOf(params.getOrDefault("userId", "0"));
        Specification<Room> spec = Specification.where(null);

        if (userId != 0) {
            UserResponse userResponse = objectMapper.convertValue(
                    userServiceClient.getUserById(userId).getBody().getData(),
                    UserResponse.class
            );

            List<Integer> roomIds = objectMapper.convertValue(
                    documentServiceClient.getRoomIdsByUserIdPaging(userId).getBody().getData(), List.class
            );
            spec.and(RoomSpecification.filterByRoomIds(roomIds));
        }

        if (!status.equals("")) {
            spec.and(RoomSpecification.hasStatus(status));
        }

        Pageable pageable = PageRequest.of(page, size);
        return roomRepo.findAll(spec, pageable);
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

    @Override
    public Page<Integer> getRoomIdsByUserId(Integer userId, int page, int size) {

        return null;
    }
}
