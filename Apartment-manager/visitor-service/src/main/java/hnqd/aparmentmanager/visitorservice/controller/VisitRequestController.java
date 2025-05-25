package hnqd.aparmentmanager.visitorservice.controller;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.visitorservice.dto.request.VisitRequestDto;
import hnqd.aparmentmanager.visitorservice.entity.VisitRequest;
import hnqd.aparmentmanager.visitorservice.service.IVisitRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/visitRequests")
public class VisitRequestController {

    private final IVisitRequestService visitRequestService;

    public VisitRequestController(IVisitRequestService visitRequestService) {
        this.visitRequestService = visitRequestService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createVisitRequest(@RequestBody VisitRequestDto visitRequestDto) {
        try {
            VisitRequest visitRequest = visitRequestService.createVisitRequest(visitRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("OK", "Create visit request successful!", visitRequest)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create visit request failed!", e.getMessage())
            );
        }
    }

    @PatchMapping("/{visitRequestId}")
    public ResponseEntity<ResponseObject> updateVisitRequest(
            @PathVariable Integer visitRequestId,
            @RequestBody Map<String, String> paramUpdate
            ) {
        try {
            paramUpdate.put("visitRequestId", String.valueOf(visitRequestId));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update visit request successful!",
                            visitRequestService.updateVisitRequest(paramUpdate)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Update visit request failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getVisitRequests(
            @RequestParam Map<String, String> paramGet
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get visit request successful!",
                            visitRequestService.getVisitRequestRes(paramGet)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get visit request failed!", e.getMessage())
            );
        }
    }

}
