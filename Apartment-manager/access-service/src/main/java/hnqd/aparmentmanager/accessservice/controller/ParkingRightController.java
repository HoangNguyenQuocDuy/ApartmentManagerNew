package hnqd.aparmentmanager.accessservice.controller;

import hnqd.aparmentmanager.accessservice.dto.ParkingRightRequest;
import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import hnqd.aparmentmanager.accessservice.service.IParkingRightService;
import hnqd.aparmentmanager.common.dto.NotifyToUserDto;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/parking-rights")
@RequiredArgsConstructor
public class ParkingRightController {

    private final IParkingRightService parkingRightService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createParkingRight(@RequestBody ParkingRightRequest pr) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                    new ResponseObject("OK", "Create parking right successfully!",
                            parkingRightService.createParkingRight(pr))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getParkingRights(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list parking rights successfully!",
                            parkingRightService.getParkingRightsPaging(params))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @PatchMapping("/{prId}")
    public ResponseEntity<ResponseObject> updateParkingRights(@PathVariable("prId") Integer prId, @RequestBody Map<String, String> params) {
        try {
            ParkingRight result = parkingRightService.updateParkingRight(prId, params);

            NotifyToUserDto notifyToUserDto = NotifyToUserDto
                    .builder()
                    .userId(Integer.parseInt(params.get("userId")))
                    .message("Your requested card has been updated!")
                    .build();
            rabbitTemplate.convertAndSend("commonNotifyExchange",
                    "H9wMk8fKtP", notifyToUserDto
            );
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update parking right successfully!", result)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{prId}")
    public ResponseEntity<ResponseObject> deleteParkingRight(@PathVariable("prId") Integer prId) {
        try {
            parkingRightService.deleteParkingRight(prId);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete parking right successfully!", ""
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

}
