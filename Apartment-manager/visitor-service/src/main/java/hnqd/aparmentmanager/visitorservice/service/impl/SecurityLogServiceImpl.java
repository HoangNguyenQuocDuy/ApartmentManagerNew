package hnqd.aparmentmanager.visitorservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.ESecurityType;
import hnqd.aparmentmanager.common.dto.response.UserResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.visitorservice.client.IUserServiceClient;
import hnqd.aparmentmanager.visitorservice.dto.request.AccessRequestDto;
import hnqd.aparmentmanager.visitorservice.entity.SecurityLog;
import hnqd.aparmentmanager.visitorservice.entity.VisitRequest;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import hnqd.aparmentmanager.visitorservice.repository.ISecurityLogRepository;
import hnqd.aparmentmanager.visitorservice.repository.IVisitRequestRepository;
import hnqd.aparmentmanager.visitorservice.repository.IVisitorRepository;
import hnqd.aparmentmanager.visitorservice.service.ISecurityLogService;
import hnqd.aparmentmanager.visitorservice.specification.SecurityLogSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SecurityLogServiceImpl implements ISecurityLogService {

    private final ISecurityLogRepository securityLogRepository;
    private final IVisitRequestRepository visitRequestRepository;
    private final IVisitorRepository visitorRepository;
    private final ObjectMapper objectMapper;
    private final IUserServiceClient userServiceClient;

    @Autowired
    public SecurityLogServiceImpl(
            ISecurityLogRepository securityLogRepository,
            IVisitRequestRepository visitRequestRepository,
            IVisitorRepository visitorRepository,
            ObjectMapper objectMapper,
            IUserServiceClient userServiceClient
    ) {
        this.securityLogRepository = securityLogRepository;
        this.visitRequestRepository = visitRequestRepository;
        this.visitorRepository = visitorRepository;
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
    }

    @Transactional
    @Override
    public CompletableFuture<SecurityLog> checkinVisitor(AccessRequestDto accessRequestDto) {

        CompletableFuture<UserResponse> userFuture = CompletableFuture.supplyAsync(() ->
                objectMapper.convertValue(
                        userServiceClient.getUserById(accessRequestDto.getStaffId()).getBody().getData(),
                        UserResponse.class
                )
        );
        CompletableFuture<Visitor> visitorFuture = CompletableFuture.supplyAsync(
                () -> visitorRepository.findByIdCardNumber(accessRequestDto.getIdCardNumber()).orElseThrow(
                        () -> new CommonException.NotFoundException("Visitor not found")
                )
        );
        CompletableFuture<VisitRequest> visitRequestFuture = CompletableFuture.supplyAsync(
                () -> visitRequestRepository.findById(accessRequestDto.getVisitRequestId()).orElseThrow(
                        () -> new CommonException.NotFoundException("Visit request not found")
                )
        );

        return userFuture
                .thenCombine(visitorFuture, (staff, visitor) -> new Object[]{staff, visitor})
                .thenCombine(visitRequestFuture, (arr, visitRequest) -> {
                    Visitor visitor = (Visitor) arr[1];

                    SecurityLog securityLog = new SecurityLog();
                    securityLog.setVisitor(visitor);
                    securityLog.setCheckinStaffId(accessRequestDto.getStaffId());
                    securityLog.setVisitRequest(visitRequest);
                    securityLog.setCheckinTime(LocalDateTime.now());
                    securityLog.setStatus(ESecurityType.CHECK_IN);

                    return securityLogRepository.save(securityLog);
                });
    }

    @Override
    public CompletableFuture<SecurityLog> checkoutVisitor(AccessRequestDto accessRequestDto) {
        CompletableFuture<UserResponse> userFuture = CompletableFuture.supplyAsync(() ->
                objectMapper.convertValue(
                        userServiceClient.getUserById(accessRequestDto.getStaffId()).getBody().getData(),
                        UserResponse.class
                )
        );
        CompletableFuture<SecurityLog> securityLogFuture = CompletableFuture.supplyAsync(
                () -> securityLogRepository.findTopByVisitorIdAndStatusOrderByCheckinTimeDesc(
                        accessRequestDto.getSecurityLogId(), ESecurityType.CHECK_IN).orElseThrow(
                                () -> new CommonException.BadRequestException("Visitor has not checked in yet!")
                )
        );

        return userFuture.thenCombine(securityLogFuture, (userResponse, securityLog) -> {
                    if (securityLog.getCheckoutTime() != null) {
                        throw new CommonException.BadRequestException("Visitor has already checked out!");
                    }

                    securityLog.setCheckoutTime(LocalDateTime.now());
                    securityLog.setCheckoutStaffId(accessRequestDto.getStaffId());
                    securityLog.setStatus(ESecurityType.CHECK_OUT);

                    return securityLogRepository.save(securityLog);
                });
    }

    @Override
    public Page<SecurityLog> getSecurityLogs(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        String idCardNumber = params.getOrDefault("idCardNumber", "");
        String status = params.getOrDefault("status", "");


        Specification<SecurityLog> specification = Specification.where(null);

        if (!status.equals("")) {
            specification = specification.and(SecurityLogSpecification.hasStatus(ESecurityType.valueOf(status)));
        }
        if (!idCardNumber.equals("")) {
            specification = specification.and(SecurityLogSpecification.hasVisitorIdCardNumber(idCardNumber));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return securityLogRepository.findAll(specification, pageable);
    }
}
