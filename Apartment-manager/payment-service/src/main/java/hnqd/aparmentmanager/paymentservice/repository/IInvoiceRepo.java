package hnqd.aparmentmanager.paymentservice.repository;

import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IInvoiceRepo extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {

    Page<Invoice> findAll(Pageable pageable);

    Page<Invoice> findAllByContractTermIdIn(List<Integer> contractTermIds, Pageable pageable);

}
