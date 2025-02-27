package hnqd.aparmentmanager.documentservice.service;

import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.ContractTerm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IContractTermService {

    Page<ContractTerm> getContractTermsPaging(Map<String, String> params);

    List<Integer> getContractTermsIdPagingByUserId(int userId);

    ContractTerm updateContractTerm(Integer id, EPaymentStatus status);

    ContractTerm createContractTerm(ContractTermDto contractTerm);

}
