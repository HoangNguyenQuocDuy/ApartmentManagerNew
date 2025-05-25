package hnqd.aparmentmanager.paymentservice.controller;

import hnqd.aparmentmanager.common.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import hnqd.aparmentmanager.paymentservice.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @Autowired
    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getInvoicesPaging(@RequestParam Map<String, String> params) {
        try {
            Page<Invoice> invoices = invoiceService.getInvoicesByUserId(params);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get invoices by userId successful!", invoices)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Get invoices by userId failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createInvoice(@RequestBody Map<String, String> invoiceReqDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Create invoice successful!",
                            invoiceService.createInvoice(invoiceReqDto)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create invoice failed!", e.getMessage())
            );
        }
    }

    @PostMapping("/for-client")
    public ResponseEntity<ResponseObject> createInvoiceClient(@RequestBody InvoiceReqDto invoiceReqDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Create invoice successful!",
                            invoiceService.createInvoice(invoiceReqDto)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create invoice failed!", e.getMessage())
            );
        }
    }

    @PatchMapping("/{invoiceId}")
    public ResponseEntity<ResponseObject> updateInvoice(
            @PathVariable("invoiceId") Integer invoiceId,
            @RequestBody Map<String, String> params
            ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Create invoice successful!",
                            invoiceService.updateInvoice(invoiceId, params)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Create invoice failed!", e.getMessage())
            );
        }
    }

}
