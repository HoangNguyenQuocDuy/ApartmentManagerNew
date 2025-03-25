package hnqd.aparmentmanager.documentservice.service;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.dto.request.GetContractForRelativeRequest;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IContractService {

    Page<Contract> getContractsPaging(Map<String, String> params);

    Contract creaeContract(ContractDto contractDto);

    Contract updateContract(Integer id, EContractStatus status);

    Page<Contract> getContractsByUserId(Integer userId, int page, int size);

    List<Integer> getRoomIdsByUserId(Integer userId);

    Integer getUserIdByRoomId(Integer roomId);

    ContractResponse getContractById(Integer contractId);

    ContractResponse getContractByRoomIdAndUserId(GetContractForRelativeRequest request);

    List<Integer> getContractIdsByUserId(Integer userId);

    List<Integer> getContractIdsByRoomId(Integer roomId);

}
