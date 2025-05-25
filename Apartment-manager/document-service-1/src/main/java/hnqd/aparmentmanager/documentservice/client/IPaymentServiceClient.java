package hnqd.aparmentmanager.documentservice.client;

import hnqd.aparmentmanager.common.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:9010")
public interface IPaymentServiceClient {

    String BASE = "/api/invoices";

    @PostMapping(BASE + "/for-client")
    ResponseEntity<ResponseObject> createInvoice(@RequestBody InvoiceReqDto invoiceReqDto);

}
