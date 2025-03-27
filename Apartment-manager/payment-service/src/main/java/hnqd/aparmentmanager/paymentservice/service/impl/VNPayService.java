package hnqd.aparmentmanager.paymentservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EEmailType;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.Enum.EProvider;
import hnqd.aparmentmanager.common.dto.request.PaymentRequest;
import hnqd.aparmentmanager.common.dto.response.PaymentResponse;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.paymentservice.client.IUserServiceClient;
import hnqd.aparmentmanager.paymentservice.config.VNPayConfig;
import hnqd.aparmentmanager.paymentservice.dto.response.UserResponse;
import hnqd.aparmentmanager.paymentservice.entity.Payment;
import hnqd.aparmentmanager.paymentservice.repository.IPaymentRepo;
import hnqd.aparmentmanager.paymentservice.service.IPayService;
import hnqd.aparmentmanager.paymentservice.service.IPaymentCallbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayService implements IPayService, IPaymentCallbackService {

    private final ObjectMapper objectMapper;
    private final VNPayConfig vnPayConfig;
    private final IPaymentRepo paymentRepo;
    private final ObjectMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final IUserServiceClient userServiceClient;

    public VNPayService(
            ObjectMapper objectMapper,
            IPaymentRepo paymentRepo,
            VNPayConfig vnPayConfig,
            ObjectMapper mapper,
            RabbitTemplate rabbitTemplate,
            IUserServiceClient userServiceClient
    ) {
        this.objectMapper = objectMapper;
        this.paymentRepo = paymentRepo;
        this.vnPayConfig = vnPayConfig;
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.userServiceClient = userServiceClient;
    }

    @Transactional
    @Override
    public PaymentResponse createPaymentUrl(HttpServletRequest req, PaymentRequest paymentRequest) throws UnsupportedEncodingException, JsonProcessingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        Map<String, String> vnp_OrderInfo = new HashMap<>();
        double amount = Integer.parseInt(req.getParameter("amount"));
        Integer userId = Optional.ofNullable(paymentRequest.getUserId()).orElseThrow(() ->
                new CommonException.IllegalArgument("UserId is required!")
        );
        Integer invoiceId = Optional.ofNullable(paymentRequest.getInvoiceId()).orElseThrow(() ->
                new CommonException.IllegalArgument("InvoiceId is required!")
        );
        Payment payment = Payment
                .builder()
                .transactionId(UUID.randomUUID().toString())
                .status(EPaymentStatus.PENDING.getName())
                .amount(amount * 100)
                .provider(EProvider.VNPAY.getName())
                .userId(userId)
                .invoiceId(invoiceId)
                .build();
        Payment savePayment = paymentRepo.save(payment);

        UserResponse userResponse = objectMapper.convertValue(
                userServiceClient.getUserById(userId).getBody().getData(),
                UserResponse.class
        );

        vnp_OrderInfo.put("paymentId", savePayment.getId().toString());
        vnp_OrderInfo.put("email", userResponse.getEmail());
        String orderType = "order_type";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = vnPayConfig.getVnp_tmnCode();

        Map vnp_Params = new HashMap();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(10000 * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", mapper.writeValueAsString(vnp_OrderInfo));
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_returnurl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);


//        todo: execute later
//        String callbackUrl = "http://localhost:8080/api/invoices/payemtResult/";
//        vnp_Params.put("vnp_ReturnUrl", callbackUrl);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        System.out.println("VNP Hash Secret: " + vnPayConfig.getVnp_hashsecret());

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnPayConfig.getVnp_hashsecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnp_payurl() + "?" + queryUrl;

        return PaymentResponse.builder()
                .status("OK")
                .message("Successfully")
                .URL(paymentUrl)
                .build();
    }

    @Override
    public ResponseObject handleCallback(Map<String, String> params) throws JsonProcessingException {
        String vnp_TmnCode = params.getOrDefault("vnp_TmnCode", "");
        Long vnp_Amount = Long.parseLong(params.getOrDefault("vnp_Amount", "0"));
        String vnp_BankCode = params.getOrDefault("vnp_BankCode", "");
        String vnp_BankTranNo = params.getOrDefault("vnp_BankTranNo", "");
        String vnp_CardType = params.getOrDefault("vnp_CardType", "");
        Long vnp_PayDate = Long.parseLong(params.getOrDefault("vnp_PayDate", "0"));
        String vnp_OrderInfo = params.getOrDefault("vnp_OrderInfo", "");
        Long vnp_TransactionNo = Long.parseLong(params.getOrDefault("vnp_TransactionNo", "0"));
        Long vnp_ResponseCode = Long.parseLong(params.getOrDefault("vnp_ResponseCode", "0"));
        Long vnp_TransactionStatus = Long.parseLong(params.getOrDefault("vnp_TransactionStatus", "0"));
        String vnp_TxnRef = params.getOrDefault("vnp_TxnRef", "");
        String vnp_SecureHash = params.getOrDefault("vnp_SecureHash", "");
        Map<String, String> orderInfo = objectMapper.readValue(vnp_OrderInfo, Map.class);

        String paymentId = orderInfo.getOrDefault("paymentId", "0");

        if (!paymentId.equals("0")) {
            Payment payment = paymentRepo.findById(paymentId).orElseThrow(
                    () -> new CommonException.NotFoundException("Payment not found!")
            );

            Map<String, Object> extraData = new HashMap<>();
            extraData.put("vnp_TmnCode", vnp_TmnCode);
            extraData.put("vnp_Amount", vnp_Amount);
            extraData.put("vnp_BankCode", vnp_BankCode);
            extraData.put("vnp_BankTranNo", vnp_BankTranNo);
            extraData.put("vnp_CardType", vnp_CardType);
            extraData.put("vnp_PayDate", vnp_PayDate);
            extraData.put("vnp_TransactionNo", vnp_TransactionNo);
            extraData.put("vnp_ResponseCode", vnp_ResponseCode);
            extraData.put("vnp_TransactionStatus", vnp_TransactionStatus);
            extraData.put("vnp_TxnRef", vnp_TxnRef);
            extraData.put("vnp_SecureHash", vnp_SecureHash);
            payment.setExtraData(extraData);
            payment.setStatus(EPaymentStatus.COMPLETED.getName());

            Map<String, String> paramMessage = new HashMap<>();

            LocalDateTime currentDate = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
            String formatPaymentDate = currentDate.format(formatter);
            String email = orderInfo.getOrDefault("email", "");

            paramMessage.put("paymentDate", formatPaymentDate);
            paramMessage.put("amount", vnp_Amount.toString());
            paramMessage.put("transactionId", payment.getTransactionId());
            paramMessage.put("email", email);
            paramMessage.put("mailType", EEmailType.PAYMENT_SUCCESS.getName());
            paramMessage.put("subject", "PAYMENT FOR INVOICE - QUỐC DUY APARTMENT \uD83C\uDFD8\uFE0F");

            rabbitTemplate.convertAndSend(
                    "notificationExchange",
                    "rSc7D1FNUS",
                    paramMessage,
                    message -> {
                        message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
                        return message;
                    }
            );

            return new ResponseObject("OK!", "Payment successful!", paymentRepo.save(payment));
        }

        return new ResponseObject("FAILED!", "Payment failed!", "");
    }

}
