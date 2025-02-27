package hnqd.aparmentmanager.documentservice.service.impl;

import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.entity.ContractTerm;
import hnqd.aparmentmanager.documentservice.repository.IContractRepo;
import hnqd.aparmentmanager.documentservice.repository.IContractTermRepo;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContractTermServiceImpl implements IContractTermService {

    private final IContractTermRepo contractTermRepo;
    private final IContractRepo contractRepo;

    @Autowired
    public ContractTermServiceImpl(
            IContractTermRepo contractTermRepo,
            IContractRepo contractRepo
    ) {
        this.contractTermRepo = contractTermRepo;
        this.contractRepo = contractRepo;
    }

    @Override
    public Page<ContractTerm> getContractTermsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);

        return contractTermRepo.findAll(pageable);
    }

    @Override
    public List<Integer> getContractTermsIdPagingByUserId(int userId) {
        return contractTermRepo.findContractTermIdsByUserId(userId);
    }

    @Override
    public ContractTerm updateContractTerm(Integer id, EPaymentStatus status) {
        ContractTerm contractTerm = contractTermRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("ContractTerm with id " + id + " not found")
        );

        contractTerm.setPaymentStatus(status);

        return contractTermRepo.save(contractTerm);
    }

    @Override
    public ContractTerm createContractTerm(ContractTermDto contractTermDto) {
        Contract contract = contractRepo.findById(contractTermDto.getContractId()).orElseThrow(
                () -> new CommonException.NotFoundException(
                        "Contract with id " + contractTermDto.getContractId() + " not found"
                )
        );

        ContractTerm newContractTerm = new ContractTerm();
        newContractTerm.setContract(contract);
        newContractTerm.setTermAmount(contractTermDto.getTermAmount());
        newContractTerm.setTermStartDate(contractTermDto.getTermStartDate());
        newContractTerm.setTermEndDate(contractTermDto.getTermEndDate());
        newContractTerm.setPaymentStatus(EPaymentStatus.PENDING);

        return contractTermRepo.save(newContractTerm);
    }
}
