package hnqd.aparmentmanager.documentservice.service;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IContractService {

    Page<Contract> getContractsPaging(Map<String, String> params);

    Contract creaeContract(ContractDto contractDto);

    Contract updateContract(Integer id, EContractStatus status);

    Page<Contract> getContractsByUserId(Integer userId, int page, int size);

    List<Integer> getRoomIdsByUserId(Integer userId);

}
