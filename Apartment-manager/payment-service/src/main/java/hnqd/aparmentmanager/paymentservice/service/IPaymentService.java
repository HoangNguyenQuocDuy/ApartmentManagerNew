package hnqd.aparmentmanager.paymentservice.service;

import hnqd.aparmentmanager.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IPaymentService {

    Page<Payment> getListPayment(Map<String, String> params);

}
