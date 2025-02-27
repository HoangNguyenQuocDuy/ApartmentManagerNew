package hnqd.aparmentmanager.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hnqd.aparmentmanager.common.dto.request.PaymentRequest;
import hnqd.aparmentmanager.common.dto.response.PaymentResponse;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPayService {

    PaymentResponse createPaymentUrl(HttpServletRequest req, PaymentRequest paymentRequest) throws UnsupportedEncodingException, JsonProcessingException;

//    ResponseObject handleVNPayIpn(
//            String vnp_TmnCode,
//            Long vnp_Amount,
//            String vnp_BankCode,
//            String vnp_BankTranNo,
//            Long vnp_CardType,
//            Long vnp_PayDate,
//            String vnp_OrderInfo,
//            Long vnp_TransactionNo,
//            Long vnp_ResponseCode,
//            Long vnp_TransactionStatus,
//            String vnp_TxnRef,
//            String vnp_SecureHash
//    ) throws JsonProcessingException;

}
