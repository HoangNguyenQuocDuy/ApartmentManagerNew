package hnqd.aparmentmanager.paymentservice.repository;

import hnqd.aparmentmanager.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepo extends MongoRepository<Payment, String> {
}
