package hnqd.aparmentmanager.documentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.Enum.EContractType;
import hnqd.aparmentmanager.common.Enum.ERoomStatus;
import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import hnqd.aparmentmanager.common.dto.request.GetContractForRelativeRequest;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.documentservice.client.IRoomServiceClient;
import hnqd.aparmentmanager.documentservice.dto.ContractDto;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.repository.IContractRepo;
import hnqd.aparmentmanager.documentservice.service.IContractService;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import hnqd.apartmentmanager.roomservice.dto.ResponseObject;
import hnqd.apartmentmanager.roomservice.dto.RoomRequest;
import hnqd.apartmentmanager.roomservice.service.IRoomService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements IContractService {
    private final IContractRepo contractRepo;
    private final ObjectMapper objectMapper;
    private final IRoomServiceClient roomService;
    private final IContractTermService contractTermService;

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

    @Transactional
    @Override
    public Contract creaeContract(ContractDto contractDto) {
        Contract contract = new Contract();
        ContractTermDto firstTerm = new ContractTermDto();

        contract.setUserId(contractDto.getCustomerId());
        contract.setStartDate(contractDto.getStartDate());
        contract.setEndDate(contractDto.getEndDate());
        contract.setContractType(contractDto.getContractType());
        contract.setStatus(EContractStatus.PENDING);
//        contract.setTotalAmount(contractDto.getTotalAmount());
        contract.setRoomId(contractDto.getRoomId());
        contract.setContractNumber(generateContractNumber());
        contract.setTotalAmount(contractDto.getTotalAmount());

        long totalTerms = 0;
        ChronoUnit unit = ChronoUnit.MONTHS;
        int unitStep = 0;

        switch (contractDto.getContractType()) {
            case RENTAL:
                contract.setTermFrequency(ETermFrequency.MONTHLY);
                totalTerms = ChronoUnit.MONTHS.between(contractDto.getStartDate(), contractDto.getEndDate());
                firstTerm.setTermAmount(contractDto.getTotalAmount());
                break;

            case BUY:
                contract.setTermFrequency(contractDto.getTermFrequency());

                switch (contractDto.getTermFrequency()) {
                    case MONTHLY:
                        unit = ChronoUnit.MONTHS;
                        unitStep = 1;
                        totalTerms = ChronoUnit.MONTHS.between(contractDto.getStartDate(), contractDto.getEndDate());
                        break;
                    case QUARTERLY:
                        unit = ChronoUnit.MONTHS;
                        unitStep = 3;
                        totalTerms = ChronoUnit.MONTHS.between(contractDto.getStartDate(), contractDto.getEndDate()) / 3;
                        break;
                    case YEARLY:
                        unit = ChronoUnit.YEARS;
                        unitStep = 1;
                        totalTerms = ChronoUnit.YEARS.between(contractDto.getStartDate(), contractDto.getEndDate());
                        break;

                    default:
                        unit = ChronoUnit.MONTHS;
                        unitStep = 1;
                        totalTerms = ChronoUnit.MONTHS.between(contractDto.getStartDate(), contractDto.getEndDate());
                        break;
                }

                BigDecimal perTermAmount = contractDto.getTotalAmount()
                        .divide(BigDecimal.valueOf(totalTerms), RoundingMode.HALF_UP);
                firstTerm.setTermAmount(perTermAmount);

            default:
                break;
        }

//        contractTermDto.setContractId(contractSave.getId());
//        contractTermDto.setTermStartDate(contractSave.getStartDate());
//        contractTermDto.setTermOrder(1);
//        contractTermDto.setTermEndDate(contractSave.getEndDate());
//        contractTermDto.setTermAmount(BigDecimal.valueOf(498172.3));
        contract.setTotalTerms(totalTerms);
        Contract contractSave = contractRepo.save(contract);

        LocalDateTime termStart = contractDto.getStartDate();
        LocalDateTime termEnd = termStart.plus(unitStep, unit).minusDays(1);

        firstTerm.setContractId(contractSave.getId());
        firstTerm.setTermOrder(1);
        firstTerm.setTermStartDate(termStart);
        firstTerm.setTermEndDate(termEnd);

        contractTermService.createContractTerm(firstTerm);

        RoomRequest rq = RoomRequest.builder().roomStatus(ERoomStatus.OCCUPIED).build();
        roomService.updateRoom(contractDto.getRoomId(), rq);

        return contractSave;
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
        Contract contract = contractRepo.findByUserIdAndRoomIdAndStatus(request.getUserId(), request.getRoomId(), request.getStatus());

        return objectMapper.convertValue(
                contract,
                ContractResponse.class
        );
    }

    @Override
    public List<Integer> getContractIdsByUserId(Integer userId) {
        return contractRepo.findContractIdsByUserIdAndStatus(userId, EContractStatus.ACTIVE);
    }

    @Override
    public List<Integer> getContractIdsByRoomId(Integer roomId) {
        return contractRepo.findAllByRoomIdAndStatus(roomId, EContractStatus.ACTIVE);
    }

    @Override
    public List<Integer> getRoomIdsInActiveContracts() {
        LocalDateTime now = LocalDateTime.now();

        return contractRepo.findAllByEndDateAfter(now);
    }

    @Override
    public RestResponse<ListResponse<ContractResponse>> getListContract(int page, int size, String sort, String filter, String search, boolean all) {
        Specification<Contract> sortable = RSQLJPASupport.toSort(sort);
        Specification<Contract> filterable = RSQLJPASupport.toSpecification(filter);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<Contract> resultPage = contractRepo.findAll(sortable.and(filterable), pageable);

        Page<ContractResponse> responsePage = resultPage.map(contract -> objectMapper.convertValue(contract, ContractResponse.class));

        return RestResponse.ok(ListResponse.of(responsePage));
    }

    private String generateContractNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = contractRepo.count();
        return "CT-" + datePart + "-" + String.format("%05d", count + 1);
    }
}
