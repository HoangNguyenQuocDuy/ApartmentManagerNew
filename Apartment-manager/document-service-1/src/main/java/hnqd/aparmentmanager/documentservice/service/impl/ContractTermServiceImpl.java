package hnqd.aparmentmanager.documentservice.service.impl;

import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import hnqd.aparmentmanager.common.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.documentservice.client.IPaymentServiceClient;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.entity.ContractTerm;
import hnqd.aparmentmanager.documentservice.repository.IContractRepo;
import hnqd.aparmentmanager.documentservice.repository.IContractTermRepo;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractTermServiceImpl implements IContractTermService {

    private final IContractTermRepo contractTermRepo;
    private final IContractRepo contractRepo;
    private final IPaymentServiceClient paymentServiceClient;

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
        newContractTerm.setTermOrder(contractTermDto.getTermOrder());
        contractTermRepo.save(newContractTerm);

        InvoiceReqDto invoiceReqDto = InvoiceReqDto
                .builder()
                .contractTermId(newContractTerm.getId())
                .amount(contractTermDto.getTermAmount())
                .dueDate(contractTermDto.getTermStartDate())
                .invoiceType(EInvoiceType.ROOM)
                .roomId(newContractTerm.getContract().getRoomId())
                .description("Invoice for contract term")
                .build();

        paymentServiceClient.createInvoice(invoiceReqDto);

        return newContractTerm;
    }

    @Override
    public ContractTerm getContractTermById(Integer id) {
        return contractTermRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("ContractTerm with id " + id + " not found")
        );
    }

    @Override
    public ContractTerm updateContractTermById(Integer id, String status) {
        ContractTerm contractTerm = contractTermRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("ContractTerm with id " + id + " not found")
        );

        contractTerm.setPaymentStatus(EPaymentStatus.safeValueOfName(status));

        return contractTermRepo.save(contractTerm);
    }

}
