package hnqd.aparmentmanager.paymentservice.controller;

import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.dto.request.PaymentRequest;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.paymentservice.entity.Payment;
import hnqd.aparmentmanager.paymentservice.repository.IPaymentRepo;
import hnqd.aparmentmanager.paymentservice.service.IPayService;
import hnqd.aparmentmanager.paymentservice.service.IPaymentCallbackService;
import hnqd.aparmentmanager.paymentservice.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IPaymentService paymentService;
    private final IPayService payService;
    private final IPaymentCallbackService callbackService;
    private final IPaymentRepo paymentRepo;

    @Autowired
    public PaymentController(
            IPaymentService paymentService,
            @Qualifier("VNPayService")
            IPayService payService,
            IPaymentCallbackService callbackService,
            IPaymentRepo paymentRepo
    ) {
        this.paymentService = paymentService;
        this.payService = payService;
        this.callbackService = callbackService;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/")
    public ResponseEntity<?> getContractsPaging(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get payments successful!",
                            paymentService.getListPayment(params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get payments by userId failed!", e.getMessage())
            );
        }
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<?> updatePayment(
            @PathVariable("paymentId") String paymentId,
            @RequestBody String status
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get payments successful!",
                            paymentService.updatePaymentManual(paymentId, status)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get payments by userId failed!", e.getMessage())
            );
        }
    }

//    @RequestParam String vnp_TmnCode,
//    @RequestParam Long vnp_Amount,
//    @RequestParam String vnp_BankCode,
//    @RequestParam String vnp_BankTranNo,
//    @RequestParam Long vnp_CardType,
//    @RequestParam Long vnp_PayDate,
//    @RequestParam String vnp_OrderInfo,
//    @RequestParam Long vnp_TransactionNo,
//    @RequestParam Long vnp_ResponseCode,
//    @RequestParam Long vnp_TransactionStatus,
//    @RequestParam String vnp_TxnRef,
//    @RequestParam String vnp_SecureHash

    @GetMapping("/callback")
    public ResponseEntity<?> callbackPayment(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Callback payment successful!", callbackService.handleCallback(params))
            );
        } catch (Exception e) {
            Payment payment = paymentRepo.findById(params.get("paymentId")).orElseThrow(
                    () -> new CommonException.NotFoundException("Payment not found")
            );

            payment.setStatus(EPaymentStatus.FAILED.getName());
            paymentRepo.save(payment);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Callback payment failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payInvoice(HttpServletRequest req, @RequestBody PaymentRequest paymentRequest) throws UnsupportedEncodingException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Pay invoice successful!",
                            payService.createPaymentUrl(req, paymentRequest)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Pay invoice failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/manual")
    public ResponseEntity<?> createPayment(
            @RequestPart MultipartFile file,
            @RequestParam Map<String, String> params) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.createPaymentManual(file, params));
    }
}
