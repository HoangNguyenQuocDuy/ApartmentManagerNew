package hnqd.aparmentmanager.authservice.client;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chat-service", url = "http://localhost:9011")
public interface IChatRoomService {
    String BASE = "/api/chat/rooms";

    @PostMapping(BASE + "/with-admin")
    ResponseEntity<ResponseObject> createOrGetChatRoomWithAdmin(@RequestParam int userId);

    @PostMapping(BASE + "/with-common")
    ResponseEntity<ResponseObject> createOrGetChatRoomWithCommonGroup(@RequestParam Integer userId);

}
