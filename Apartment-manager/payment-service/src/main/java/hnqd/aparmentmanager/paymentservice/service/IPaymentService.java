package hnqd.aparmentmanager.paymentservice.service;

import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IPaymentService {

    Page<Payment> getListPayment(Map<String, String> params);

    Payment createPaymentManual(MultipartFile file, Map<String, String> params) throws IOException;

    RestResponse<ListResponse<Payment>> getListPayment(int page, int size,
                                                                 String sort, String filter,
                                                                 String search, boolean all);

    Payment updatePaymentManual(String id, String status);
}
