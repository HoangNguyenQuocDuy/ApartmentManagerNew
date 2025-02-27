package hnqd.aparmentmanager.documentservice.controller;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.service.IContractService;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final IContractService contractService;
    private final IContractTermService contractTermService;

    @Autowired
    public ContractController(
            IContractService contractService,
            IContractTermService contractTermService
    ) {
        this.contractService = contractService;
        this.contractTermService = contractTermService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getContractsPaging(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contracts successfully!",
                            contractService.getContractsPaging(params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get contracts paging failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/room/user/{userId}")
    public ResponseEntity<?> getRoomIdsByUserIdPaging(@PathVariable("userId") Integer userId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get roomIds by userId paging successfully!",
                            contractService.getRoomIdsByUserId(userId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get roomIds by userId paging failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createContract(@RequestBody ContractDto contractDto) {
        try {
            Contract contractSave = contractService.creaeContract(contractDto);

            ContractTermDto contractTermDto = new ContractTermDto();
            contractTermDto.setContractId(contractSave.getId());
            contractTermDto.setTermStartDate(contractSave.getStartDate());
            contractTermDto.setTermEndDate(contractSave.getEndDate());
            contractTermDto.setTermAmount(BigDecimal.valueOf(498172.3));

            contractTermService.createContractTerm(contractTermDto);

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Create contract successfully!",
                            contractSave
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create contract failed!", e.getMessage())
            );
        }
    }

    @PatchMapping("/{contractId}")
    public ResponseEntity<ResponseObject> updateContract(
            @PathVariable("contractId") Integer contractId,
            @RequestBody EContractStatus contractStatus
            ) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Update contract successfully!",
                            contractService.updateContract(contractId, contractStatus)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Update contract failed!", e.getMessage())
            );
        }
    }

}
