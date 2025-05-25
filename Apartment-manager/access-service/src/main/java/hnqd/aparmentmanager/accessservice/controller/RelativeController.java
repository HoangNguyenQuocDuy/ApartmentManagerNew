package hnqd.aparmentmanager.accessservice.controller;

import hnqd.aparmentmanager.accessservice.dto.RelativeRequest;
import hnqd.aparmentmanager.accessservice.service.IRelativeService;
import hnqd.aparmentmanager.common.Enum.ERelativeType;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.Map;

@RestController
@RequestMapping("/api/relative")
public class RelativeController {

    private final IRelativeService relativeService;

    @Autowired
    public RelativeController(IRelativeService relativeService) {
        this.relativeService = relativeService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createRelative(
            @RequestParam String fullName,
            @RequestParam ERelativeType relationship,
            @RequestPart MultipartFile file,
            @RequestParam String idCard,
            @RequestParam Integer roomId,
            @RequestParam Integer userId
    ) {
        try {
            RelativeRequest relativeRequest = new RelativeRequest();
            relativeRequest.setFullName(fullName);
            relativeRequest.setRelationship(relationship);
            relativeRequest.setIdCard(idCard);
            relativeRequest.setRoomId(roomId);
            relativeRequest.setUserId(userId);
            relativeRequest.setFile(file);

            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                    new ResponseObject("OK", "Create user successfully!", relativeService.createRelative(relativeRequest))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

//    @RequestParam(defaultValue = "0") int page,
//    @RequestParam(defaultValue = "5") int size,
//    @RequestParam(defaultValue = "id, desc") String sort,
//    @RequestParam(required = false) String filter,
//    @RequestParam(required = false) String search,
//    @RequestParam(required = false) boolean all
    @GetMapping("/")
    public ResponseEntity<ResponseObject> getRelatives(@RequestParam Map<String, String> query) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list relative successfully!",
                            relativeService.getListRelative(query)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @PatchMapping("/{relativeId}")
    public ResponseEntity<ResponseObject> updateRelative(@PathVariable("relativeId") int relativeId,
                                                         @RequestPart @Nullable MultipartFile file, @RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update relative successfully!",
                            relativeService.updateRelative(relativeId, file, params))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{relativeId}")
    public ResponseEntity<ResponseObject> deleteParkingRight(@PathVariable("relativeId") Integer relativeId) {
        try {
            relativeService.deleteRelative(relativeId);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Delete relative successfully!", ""
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(
                    new ResponseObject("FAILED", e.getMessage(), "")
            );
        }
    }

}
