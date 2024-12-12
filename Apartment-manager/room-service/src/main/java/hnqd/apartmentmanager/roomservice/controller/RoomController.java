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
        List<Room> rooms = roomService.getRooms(params);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Get rooms successfully!", rooms)
        );
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getRoomsPaging(@RequestParam Map<String, String> params) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Get rooms successfully!", roomService.getRoomsPaging(params))
        );
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ResponseObject> updateRoom(
            @PathVariable Integer roomId,
            @ModelAttribute RoomRequest roomReq) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Update rooms successfully!", roomService.updateRoom(roomReq, roomId))
        );
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ResponseObject> deleteRoom(@PathVariable Integer roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Update rooms successfully!", "")
        );
    }
}
