package hnqd.aparmentmanager.accessservice.controller;

import hnqd.aparmentmanager.accessservice.dto.EntryRightRequest;
import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import hnqd.aparmentmanager.accessservice.service.IEntryRightService;
import hnqd.aparmentmanager.common.dto.NotifyToUserDto;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/entry-rights")
@RequiredArgsConstructor
public class EntryRightController {

    private final IEntryRightService entryRightService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createEntryRight(@RequestBody EntryRightRequest pr) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                    new ResponseObject("OK", "Create entry right successfully!",
                            entryRightService.createEntryRight(pr))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getEntryRights(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list entry rights successfully!",
                            entryRightService.getEntryRightsPaging(params))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @PatchMapping("/{erId}")
    public ResponseEntity<ResponseObject> updateEntryRights(@PathVariable("erId") Integer erId , @RequestBody Map<String, String> params) {
        try {

            EntryRight result = entryRightService.updateEntryRight(erId, params);
            NotifyToUserDto notifyToUserDto = NotifyToUserDto
                    .builder()
                    .userId(Integer.parseInt(params.get("userId")))
                    .message("Your requested card has been updated!")
                    .build();
            rabbitTemplate.convertAndSend("commonNotifyExchange",
                    "H9wMk8fKtP", notifyToUserDto
            );
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update entry right successfully!", result)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{erId}")
    public ResponseEntity<ResponseObject> deleteEntryRight(@PathVariable("erId") Integer erId) {
        try {
            entryRightService.deleteEntryRight(erId);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete entry right successfully!", ""
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

}
