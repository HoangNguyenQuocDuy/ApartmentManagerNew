package hnqd.apartmentmanager.roomservice.controller;

import hnqd.apartmentmanager.roomservice.dto.ResponseObject;
import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import hnqd.apartmentmanager.roomservice.entity.Room;
import hnqd.apartmentmanager.roomservice.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createRoom(
            @RequestParam String name,
            @RequestParam Integer roomTypeId,
            @RequestParam MultipartFile file
    ) {
        try {
            RoomRequest roomReq = RoomRequest
                    .builder()
                    .name(name)
                    .roomTypeId(roomTypeId)
                    .file(file)
                    .build();
            Room roomSave = roomService.createRoom(roomReq);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("OK", "Create room successfully!", roomSave)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create room failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseObject> getRooms(@RequestParam Map<String, String> params) {
        try {
            List<Room> rooms = roomService.getRooms(params);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get rooms successfully!", rooms)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get rooms failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseObject> getRoomById(@PathVariable("roomId") Integer roomId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get room by Id successfully!", roomService.getRoomById(roomId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get room failed!", e.getMessage())
            );
        }
    }

//    Nguyệt Quế phiền phức

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getRoomsPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id, desc") String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get rooms successfully!",
                            roomService.getListRoom(page, size, sort, filter, search, all))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get rooms paging failed!", e.getMessage())
            );
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ResponseObject> updateRoom(
            @PathVariable Integer roomId,
            @RequestBody RoomRequest roomReq) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update room successfully!", roomService.updateRoom(roomReq, roomId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Update room failed!", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ResponseObject> deleteRoom(@PathVariable Integer roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete room successfully!", "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Delete room failed!", e.getMessage())
            );
        }
    }

}
