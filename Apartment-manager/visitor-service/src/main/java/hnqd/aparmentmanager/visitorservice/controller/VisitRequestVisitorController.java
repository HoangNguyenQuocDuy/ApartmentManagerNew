package hnqd.aparmentmanager.visitorservice.controller;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.visitorservice.service.IVisitRequestVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/visitRequestVisitor")
@RequiredArgsConstructor
public class VisitRequestVisitorController {

    private final IVisitRequestVisitorService visitRequestVisitorService;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getContractsPaging(
            @RequestParam Map<String, String> params
    ) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get list visitRequestVisitor successfully!",
                            visitRequestVisitorService.getListVisitRequestVisitor(params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get list visitRequestVisitor failed!", e.getMessage())
            );
        }
    }

}
