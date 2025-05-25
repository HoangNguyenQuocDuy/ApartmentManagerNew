package hnqd.aparmentmanager.documentservice.client;

import hnqd.apartmentmanager.roomservice.dto.ResponseObject;
import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "room-service", url = "http://localhost:9002")
public interface IRoomServiceClient {

    String BASE = "/api/rooms";

    @PutMapping(BASE + "/{roomId}")
    ResponseEntity<ResponseObject> updateRoom(@PathVariable Integer roomId, @RequestBody RoomRequest roomReq);

}
