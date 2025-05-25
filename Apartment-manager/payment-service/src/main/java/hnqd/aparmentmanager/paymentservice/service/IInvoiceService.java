package hnqd.aparmentmanager.paymentservice.service;

import hnqd.aparmentmanager.common.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IInvoiceService {

    Page<Invoice> getInvoicesByUserId(Map<String, String> params);

    Invoice createInvoice(Map<String, String> invoiceReq);

    Invoice createInvoice(InvoiceReqDto invoiceReq);

    Invoice updateInvoice(int invoiceId, Map<String, String> params);

}
