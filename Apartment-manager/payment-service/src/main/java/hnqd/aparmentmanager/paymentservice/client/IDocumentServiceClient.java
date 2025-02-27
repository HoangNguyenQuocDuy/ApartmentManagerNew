package hnqd.aparmentmanager.paymentservice.client;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "document-service", url = "http://localhost:9003")
public interface IDocumentServiceClient {

    @GetMapping("/api/contract-terms/user/{userId}")
    ResponseEntity<ResponseObject> getContractTermIdsByUserId(@PathVariable("userId") Integer userId);

    @GetMapping("/api/contracts/room/user/{userId}")
    ResponseEntity<ResponseObject> getRoomIdsByUserIdPaging(@PathVariable("userId") Integer userId);

}
