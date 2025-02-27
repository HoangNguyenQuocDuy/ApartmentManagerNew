package hnqd.aparmentmanager.paymentservice.service;

import hnqd.aparmentmanager.paymentservice.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IInvoiceService {

    Page<Invoice> getInvoicesByUserId(Map<String, String> params);

    Invoice createInvoice(InvoiceReqDto invoiceReqDto);

}
