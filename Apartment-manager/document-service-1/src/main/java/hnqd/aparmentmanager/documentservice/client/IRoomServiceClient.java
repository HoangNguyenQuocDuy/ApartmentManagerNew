package hnqd.aparmentmanager.documentservice.client;

import hnqd.aparmentmanager.common.dto.request.RoomRequest;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "room-service", url = "http://localhost:9002")
public interface IRoomServiceClient {

    String BASE = "/api/rooms";

    @PutMapping(BASE + "/{roomId}")
    ResponseEntity<ResponseObject> updateRoom(@PathVariable Integer roomId, @RequestBody RoomRequest roomReq);

}
