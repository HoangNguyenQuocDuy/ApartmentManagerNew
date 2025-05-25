package hnqd.aparmentmanager.visitorservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "document-service", url = "http://localhost:9003")
public interface IDocumentServiceClient {

    String BASE = "/api/contracts";

    @GetMapping(BASE + "/room/{roomId}")
    ResponseEntity<?> getUserIdByRoomId(@PathVariable("roomId") Integer roomId);

}
