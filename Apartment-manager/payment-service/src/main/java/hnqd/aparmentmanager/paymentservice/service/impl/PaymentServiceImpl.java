package hnqd.aparmentmanager.paymentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.common.utils.UploadImage;
import hnqd.aparmentmanager.paymentservice.client.IUserServiceClient;
import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import hnqd.aparmentmanager.paymentservice.entity.Payment;
import hnqd.aparmentmanager.paymentservice.repository.IInvoiceRepo;
import hnqd.aparmentmanager.paymentservice.repository.IPaymentRepo;
import hnqd.aparmentmanager.paymentservice.service.IPaymentService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentRepo paymentRepo;
    private final IUserServiceClient userService;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    private final IInvoiceRepo invoiceRepo;
    private final UploadImage uploadImage;

    @Override
    public Page<Payment> getListPayment(Map<String, String> params) {
        Integer page = Integer.valueOf(params.getOrDefault("page", "0"));
        Integer size = Integer.valueOf(params.getOrDefault("size", "20"));
        String fromDateStr = params.getOrDefault("fromDate", null);
        String toDateStr = params.getOrDefault("toDate", null);
        Integer userId = Integer.valueOf(params.getOrDefault("userId", "0"));

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (fromDateStr != null) {
            fromDate = LocalDateTime.parse(fromDateStr, formatter);
        }
        if (toDateStr != null) {
            toDate = LocalDateTime.parse(toDateStr, formatter);
        } else if (fromDate != null) {
            toDate = LocalDateTime.now();
        }
        Criteria criteria = new Criteria();

        if (fromDate != null && toDate != null) {
            criteria.and("createdAt").gte(fromDate).lte(toDate);
        }

        if (userId != 0) {
            criteria.and("userId").is(userId);
        }

        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);

        List<Payment> payments = mongoTemplate.find(query, Payment.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Payment.class);

        return new PageImpl<>(payments, pageable, total);
    }

    @Override
    public Payment createPaymentManual(MultipartFile file, Map<String, String> params) throws IOException {
        Invoice invoiceSave = invoiceRepo.findById(Integer.parseInt(params.get("invoiceId"))).orElseThrow(
                () -> new CommonException.NotFoundException("Invoice not found!")
        );

        Payment payment = Payment
                .builder()

                .build();
        if (file != null) {
            payment.setUploadImage(uploadImage.uploadToCloudinary(file));
        }
        payment.setAmount(invoiceSave.getAmount().longValue());
        payment.setUserId(Integer.parseInt(params.get("userId")));
        invoiceSave.setPaidAt(LocalDateTime.now());
        invoiceSave.setInvoiceStatus(EPaymentStatus.PENDING);
        invoiceRepo.save(invoiceSave);

        payment.setInvoice(invoiceSave);
        return paymentRepo.save(payment);
    }

    @Override
    public RestResponse<ListResponse<Payment>> getListPayment(int page, int size, String sort, String filter, String search, boolean all) {
//        Specification<Payment> sortable = RSQLJPASupport.toSort(sort);
//        Specification<Payment> filterable = RSQLJPASupport.toSpecification(filter);
//
//        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);
//
//        Page<Payment> resultPage = paymentRepo.findAll(sortable.and(filterable), pageable);

        return null;
    }

    @Override
    public Payment updatePaymentManual(String id, String status) {
        Payment payment = paymentRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("Payment not found!")
        );
        payment.setStatus(status);

        return paymentRepo.save(payment);
    }
}
