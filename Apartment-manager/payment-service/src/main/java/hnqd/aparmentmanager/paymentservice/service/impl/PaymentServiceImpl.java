package hnqd.aparmentmanager.paymentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.paymentservice.client.IUserServiceClient;
import hnqd.aparmentmanager.paymentservice.entity.Payment;
import hnqd.aparmentmanager.paymentservice.repository.IPaymentRepo;
import hnqd.aparmentmanager.paymentservice.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentRepo paymentRepo;
    private final IUserServiceClient userService;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PaymentServiceImpl(
            IPaymentRepo paymentRepo,
            IUserServiceClient userService,
            ObjectMapper objectMapper,
            MongoTemplate mongoTemplate
    ) {
        this.paymentRepo = paymentRepo;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
    }

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
}
