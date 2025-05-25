package hnqd.aparmentmanager.visitorservice.controller;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.visitorservice.dto.request.AccessRequestDto;
import hnqd.aparmentmanager.visitorservice.service.ISecurityLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/visit_securities")
public class SecurityLogController {

    private final ISecurityLogService securityLogService;

    public SecurityLogController(ISecurityLogService securityLogService) {
        this.securityLogService = securityLogService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getSecurityLogs(
            @RequestParam Map<String, String> paramGet
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get visit security log successful!",
                            securityLogService.getSecurityLogs(paramGet)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get visit request failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/checkin")
    public ResponseEntity<ResponseObject> checkoutVisitor(@RequestBody Map<String, String> params) {
        try {
            AccessRequestDto accessRequestDto = new AccessRequestDto();
            accessRequestDto.setIdCardNumber(params.get("idCardNumber"));
            accessRequestDto.setStaffId(Integer.parseInt(params.get("staffId")));
            accessRequestDto.setVisitRequestId(Integer.parseInt(params.get("visitRequestId")));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Checkin successful!",
                            securityLogService.checkinVisitor(accessRequestDto)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Checkin failed!", e.getMessage())
            );
        }
    }

    @PatchMapping("/checkout/{securityLogId}")
    public ResponseEntity<ResponseObject> checkoutVisitor(
            @PathVariable Integer securityLogId,
            @RequestBody Map<String, String> paramUpdate
    ) {
        try {
            AccessRequestDto accessRequestDto = new AccessRequestDto();
            accessRequestDto.setSecurityLogId(securityLogId);
            accessRequestDto.setStaffId(Integer.parseInt(paramUpdate.get("staffId")));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Checkout successful!",
                            securityLogService.checkoutVisitor(accessRequestDto)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Checkout failed!", e.getMessage())
            );
        }
    }

}
