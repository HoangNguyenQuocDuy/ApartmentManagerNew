package hnqd.aparmentmanager.visitorservice.controller;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.visitorservice.service.IVisitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final IVisitorService visitorService;

    public VisitorController(IVisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getVisitors(
            @RequestParam Map<String, String> params
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get visitors successful!",
                            visitorService.getVisitors(params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get visitors failed!", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{visitorId}")
    public ResponseEntity<ResponseObject> getVisitors(@PathVariable Integer visitorId) {
        try {
            visitorService.deleteVisitor(visitorId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete visitor successful!","")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Delete visitor failed!", e.getMessage())
            );
        }
    }

}
