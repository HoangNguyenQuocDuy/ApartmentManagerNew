package hnqd.aparmentmanager.accessservice.client;

import hnqd.aparmentmanager.common.dto.request.GetContractForRelativeRequest;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "document-service", url = "http://localhost:9003")
public interface IDocumentServiceClient {

    String BASE = "/api/contracts";

    @GetMapping(BASE + "/for-creating-relative")
    ResponseEntity<ResponseObject> getContractByUserIdAndRoomIdAndStatus(@RequestParam GetContractForRelativeRequest request);

    @GetMapping(BASE + "/by-user/{userId}")
    ResponseEntity<ResponseObject> getContractIdsByUserId(@PathVariable("userId") Integer userId);

    @GetMapping(BASE + "/by-room/{roomId}")
    ResponseEntity<ResponseObject> getContractIdsByRoomId(@PathVariable("roomId") Integer roomId);

}
