package hnqd.aparmentmanager.documentservice.controller;

import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contract-terms")
public class ContractTermController {

    private final IContractTermService contractTermService;

    @Autowired
    public ContractTermController(IContractTermService contractTermService) {
        this.contractTermService = contractTermService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getContractsPaging(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contract terms successfully!",
                            contractTermService.getContractTermsPaging(params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get contract terms paging failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getContractTermIdsByUserId(@PathVariable("userId") Integer userId) {
        try {

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contract terms successfully!",
                            contractTermService.getContractTermsIdPagingByUserId(userId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get contract terms paging failed!", e.getMessage())
            );
        }
    }
}
