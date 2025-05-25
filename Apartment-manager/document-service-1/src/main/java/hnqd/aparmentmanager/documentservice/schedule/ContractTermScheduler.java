package hnqd.aparmentmanager.documentservice.schedule;

import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.Enum.ETermFrequency;
import hnqd.aparmentmanager.documentservice.dto.ContractTermDto;
import hnqd.aparmentmanager.documentservice.entity.Contract;
import hnqd.aparmentmanager.documentservice.entity.ContractTerm;
import hnqd.aparmentmanager.documentservice.repository.IContractRepo;
import hnqd.aparmentmanager.documentservice.repository.IContractTermRepo;
import hnqd.aparmentmanager.documentservice.service.IContractTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractTermScheduler {
    private final IContractTermService contractTermService;
    private final IContractTermRepo contractTermRepo;
    private final IContractRepo contractRepo;

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateNextContractTermMidnight() {
        generateNextContractTerm();
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void generateNextContractTermAt5PM() {
        generateNextContractTerm();
    }

    private void generateNextContractTerm() {
        List<Contract> listContractActive = contractRepo.findAllByStatus(EContractStatus.ACTIVE);
        for (Contract contract : listContractActive) {
            List<ContractTerm> listContractTerm = contractTermRepo.findAllByContractOrderByTermOrderDesc(contract);

            if (listContractTerm.isEmpty()) continue;

            ContractTerm latestTerm = listContractTerm.get(0);

            if (latestTerm.getTermOrder() >= contract.getTotalTerms()) continue;

            if (latestTerm.getTermEndDate().isBefore(LocalDateTime.now())
                    && latestTerm.getTermOrder() < contract.getTotalTerms()) {

                LocalDateTime nextStart = latestTerm.getTermEndDate().plusDays(1); // tránh lặp ngày
                LocalDateTime nextEnd = calculateEndDate(nextStart, contract.getTermFrequency());

                if (!nextStart.isAfter(contract.getEndDate())) {
                    ContractTermDto nextTermDto = new ContractTermDto();
                    nextTermDto.setContractId(contract.getId());
                    nextTermDto.setTermOrder(latestTerm.getTermOrder() + 1);
                    nextTermDto.setTermAmount(
                            contract.getTotalAmount().divide(BigDecimal.valueOf(contract.getTotalTerms()), RoundingMode.HALF_UP));
                    nextTermDto.setTermStartDate(nextStart);
                    nextTermDto.setTermEndDate(nextEnd);

                    contractTermService.createContractTerm(nextTermDto);
                }
            }
        }

    }

    private LocalDateTime calculateEndDate(LocalDateTime startDate, ETermFrequency freq) {
        return switch (freq) {
            case QUARTERLY -> startDate.plusMonths(3).minusDays(1);
            case YEARLY -> startDate.plusYears(1).minusDays(1);
            default -> startDate.plusMonths(1).minusDays(1);
        };
    }

}
