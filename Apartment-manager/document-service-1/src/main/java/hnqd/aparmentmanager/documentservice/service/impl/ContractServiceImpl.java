package hnqd.aparmentmanager.documentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.Enum.EContractType;
import hnqd.aparmentmanager.common.dto.request.GetContractForRelativeRequest;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.repository.IContractRepo;
import hnqd.aparmentmanager.documentservice.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service

public class ContractServiceImpl implements IContractService {
    private final IContractRepo contractRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public ContractServiceImpl(
            IContractRepo contractRepo,
            ObjectMapper objectMapper
    ) {
        this.contractRepo = contractRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<Contract> getContractsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);

        if (params.get("userId") != null && !Objects.equals(params.get("userId"), "")) {
            return contractRepo.findAllByUserId(Integer.parseInt(params.get("userId")), pageable);
        }

        return contractRepo.findAll(pageable);
    }

    @Override
    public Contract creaeContract(ContractDto contractDto) {
        Contract contract = new Contract();

        contract.setUserId(contractDto.getCustomerId());
        contract.setStartDate(contractDto.getStartDate());
        contract.setEndDate(contractDto.getEndDate());
        contract.setContractType(EContractType.OTHER);
        contract.setStatus(EContractStatus.PENDING);
        contract.setTotalAmount(contractDto.getTotalAmount());
        contract.setTermFrequency(contractDto.getTermFrequency());
        contract.setContractNumber(generateContractNumber());

        return contractRepo.save(contract);
    }

    @Override
    public Contract updateContract(Integer id, EContractStatus status) {
        Contract contract = contractRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("Contract with id " + id + " not found")
        );
        contract.setStatus(status);

        return contractRepo.save(contract);
    }

    @Override
    public Page<Contract> getContractsByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return contractRepo.findAllByUserId(userId, pageable);
    }

    @Override
    public List<Integer> getRoomIdsByUserId(Integer userId) {
        return contractRepo.getRoomIdsByUserId(userId);
    }

    @Override
    public Integer getUserIdByRoomId(Integer roomId) {
        return contractRepo.getUserIdByRoomId(roomId);
    }

    @Override
    public ContractResponse getContractById(Integer contractId) {
        Contract contract = contractRepo.findById(contractId).orElseThrow(
                () -> new CommonException.NotFoundException("Contract with id " + contractId + " not found")
        );

        return objectMapper.convertValue(contract, ContractResponse.class);
    }

    @Override
    public ContractResponse getContractByRoomIdAndUserId(GetContractForRelativeRequest request) {
        return objectMapper.convertValue(
                contractRepo.findByUserIdAndRoomIdAndStatus(request.getUserId(), request.getRoomId(), request.getStatus()),
                ContractResponse.class
        );
    }

    @Override
    public List<Integer> getContractIdsByUserId(Integer userId) {
        return contractRepo.findAllByUserIdAndStatus(userId, EContractStatus.ACTIVE);
    }

    @Override
    public List<Integer> getContractIdsByRoomId(Integer roomId) {
        return contractRepo.findAllByRoomIdAndStatus(roomId, EContractStatus.ACTIVE);
    }

    private String generateContractNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = contractRepo.count();
        return "CT-" + datePart + "-" + String.format("%05d", count + 1);
    }
}
