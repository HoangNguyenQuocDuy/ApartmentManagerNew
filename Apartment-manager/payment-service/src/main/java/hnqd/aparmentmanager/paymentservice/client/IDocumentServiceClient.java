package hnqd.aparmentmanager.paymentservice.client;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "document-service", url = "http://localhost:9003")
public interface IDocumentServiceClient {

    @GetMapping("/api/contract-terms/user/{userId}")
    ResponseEntity<ResponseObject> getContractTermIdsByUserId(@PathVariable("userId") Integer userId);

    @GetMapping("/api/contracts/room/user/{userId}")
    ResponseEntity<ResponseObject> getRoomIdsByUserIdPaging(@PathVariable("userId") Integer userId);

    @GetMapping("/api/contract-terms/{contractTermId}")
    ResponseEntity<ResponseObject> getContractTermById(@PathVariable("contractTermId") Integer contractTermId);

    @PatchMapping("/{contractTermId}")
    ResponseEntity<?> updateContractTermById(
            @PathVariable("contractTermId") Integer contractTermId,
            @RequestBody String status
    );

}
