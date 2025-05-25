package hnqd.aparmentmanager.documentservice.controller;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.dto.NotifyToUserDto;
import hnqd.aparmentmanager.common.dto.request.GetContractForRelativeRequest;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.service.IContractService;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final IContractService contractService;
    private final IContractTermService contractTermService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getContractsPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id, desc") String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) boolean all
    ) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contracts successfully!",
                            contractService.getListContract(page, size, sort, filter, search, all)
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

            NotifyToUserDto notifyToUserDto = NotifyToUserDto
                    .builder()
                    .userId(contractSave.getUserId())
                    .message("You have been received an contract!")
                    .build();
            rabbitTemplate.convertAndSend("commonNotifyExchange",
                    "H9wMk8fKtP", notifyToUserDto
            );
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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getUserIdByRoomId(@PathVariable("roomId") Integer roomId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get userId by roomId successfully!",
                            contractService.getUserIdByRoomId(roomId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get userId by roomId failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<?> getContractById(@PathVariable("contractId") Integer contractId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contract by contractId successfully!",
                            contractService.getContractById(contractId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get userId by contractId failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/forRelative")
    public ResponseEntity<?> getContractByUserIdAndRoomIdAndStatus(@RequestParam("userId") @NotNull Integer userId,
                                                                   @RequestParam("roomId") @NotNull Integer roomId,
                                                                   @RequestParam("status") @NotNull EContractStatus contractStatus) {
        try {
            GetContractForRelativeRequest request = GetContractForRelativeRequest
                    .builder()
                    .userId(userId)
                    .roomId(roomId)
                    .status(contractStatus)
                    .build();

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contract for creating relative successfully!",
                            contractService.getContractByRoomIdAndUserId(request)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get userId by contractId failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getContractIdsByUserId(@PathVariable("userId") Integer userId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contractIds by userId successfully!",
                            contractService.getContractIdsByUserId(userId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get contract ids by userId failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<?> getContractIdsByRoomId(@PathVariable("roomId") Integer roomId) {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contractIds by roomId successfully!",
                            contractService.getContractIdsByRoomId(roomId)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get contract ids by roomId failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/active-room-ids")
    public ResponseEntity<?> getRoomIdsInActiveContracts() {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get roomIds in active contract successfully!",
                            contractService.getRoomIdsInActiveContracts()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get roomIds in active contract failed!", e.getMessage())
            );
        }
    }

    @GetMapping("/contract-for-resident")
    public ResponseEntity<ResponseObject> getContractForCreateResident(@RequestParam("userId") Integer userId,
                                                                   @RequestParam("roomId") Integer roomId,
                                                                   @RequestParam("status") EContractStatus contractStatus) {
        try {
            GetContractForRelativeRequest request = GetContractForRelativeRequest
                    .builder()
                    .userId(userId)
                    .roomId(roomId)
                    .status(contractStatus)
                    .build();

            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                    new ResponseObject("OK", "Get contract for creating relative successfully!",
                            contractService.getContractByRoomIdAndUserId(request)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get userId by contractId failed!", e.getMessage())
            );
        }
    }
}
