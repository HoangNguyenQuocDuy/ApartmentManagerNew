package hnqd.aparmentmanager.accessservice.client;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://localhost:8888")
public interface IUserServiceClient {

    String BASE = "/api/users";

    @GetMapping(BASE + "/api/users/{userId}")
    ResponseEntity<ResponseObject> getUserById(@PathVariable int userId);

}
