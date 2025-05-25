package hnqd.aparmentmanager.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;

import java.util.Map;

public interface IPaymentCallbackService {

    ResponseObject handleCallback(Map<String, String> params) throws JsonProcessingException;

}
