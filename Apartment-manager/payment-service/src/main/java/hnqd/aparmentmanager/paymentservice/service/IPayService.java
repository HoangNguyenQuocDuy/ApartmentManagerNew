package hnqd.aparmentmanager.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hnqd.aparmentmanager.common.dto.request.PaymentRequest;
import hnqd.aparmentmanager.common.dto.response.PaymentResponse;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPayService {

    PaymentResponse createPaymentUrl(HttpServletRequest req, PaymentRequest paymentRequest) throws UnsupportedEncodingException, JsonProcessingException;

}
