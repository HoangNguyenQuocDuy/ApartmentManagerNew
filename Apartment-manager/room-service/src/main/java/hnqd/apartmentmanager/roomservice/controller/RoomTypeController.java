package hnqd.apartmentmanager.roomservice.controller;

import hnqd.apartmentmanager.roomservice.dto.ResponseObject;
import hnqd.apartmentmanager.roomservice.service.IRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roomTypes")
public class RoomTypeController {
    @Autowired
    private IRoomTypeService roomTypeService;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getListRoomType() {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list room type successfully!", roomTypeService.getRoomTypes())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get list room-type failed!", e.getMessage())
            );
        }
    }
}
