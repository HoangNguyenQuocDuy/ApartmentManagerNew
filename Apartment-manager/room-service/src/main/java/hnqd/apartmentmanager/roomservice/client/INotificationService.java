package hnqd.apartmentmanager.roomservice.client;

import hnqd.apartmentmanager.roomservice.dto.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:9003")
public interface INotificationService {
    @PostMapping("/send-email/welcome")
    void sendEmail(@RequestBody MessageDto messageDto);
}
