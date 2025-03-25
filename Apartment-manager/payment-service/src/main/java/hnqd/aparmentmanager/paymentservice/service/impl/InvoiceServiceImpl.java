package hnqd.aparmentmanager.paymentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.paymentservice.client.IDocumentServiceClient;
import hnqd.aparmentmanager.paymentservice.client.IUserServiceClient;
import hnqd.aparmentmanager.paymentservice.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.paymentservice.dto.response.UserResponse;
import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import hnqd.aparmentmanager.paymentservice.repository.IInvoiceRepo;
import hnqd.aparmentmanager.paymentservice.service.IInvoiceService;
import hnqd.aparmentmanager.paymentservice.specification.InvoiceSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    private final IInvoiceRepo invoiceRepo;
    private final IDocumentServiceClient documentServiceClient;
    private final IUserServiceClient userService;
    private final ObjectMapper objectMapper;

    public InvoiceServiceImpl(
            IInvoiceRepo invoiceRepo,
            IDocumentServiceClient documentServiceClient,
            IUserServiceClient userService,
            ObjectMapper objectMapper
    ) {
        this.invoiceRepo = invoiceRepo;
        this.documentServiceClient = documentServiceClient;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<Invoice> getInvoicesByUserId(Map<String, String> paramsRequest) {
        String invoiceStatus = paramsRequest.getOrDefault("invoiceStatus", null);
        String invoiceType = paramsRequest.getOrDefault("invoiceType", null);
        String fromDateStr = paramsRequest.getOrDefault("fromDate", null);
        String toDateStr = paramsRequest.getOrDefault("toDate", null);
        int userId = Integer.valueOf(paramsRequest.getOrDefault("userId", "0"));
        int page = Integer.valueOf(paramsRequest.getOrDefault("page", "0"));
        int size = Integer.valueOf(paramsRequest.getOrDefault("size", "20"));

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (fromDateStr != null) {
            fromDate = LocalDateTime.parse(fromDateStr, formatter);
        }
        if (toDateStr != null) {
            toDate = LocalDateTime.parse(toDateStr, formatter);
        }

        Specification<Invoice> specification = Specification
                .where(InvoiceSpecification.hasInvoiceStatus(invoiceStatus))
                .and(InvoiceSpecification.hasInvoiceType(invoiceType))
                .and(InvoiceSpecification.hasCreatedAtBetween(fromDate, toDate));

        if (userId != 0) {
            UserResponse userResponse = objectMapper.convertValue(
                    userService.getUserById(userId).getBody().getData(),
                    UserResponse.class
            );

            List<Integer> contractTermIds = objectMapper.convertValue(
                    documentServiceClient.getContractTermIdsByUserId(userId).getBody().getData(),
                    List.class
            );
            List<Integer> roomTermIds = objectMapper.convertValue(
                    documentServiceClient.getRoomIdsByUserIdPaging(userId).getBody().getData(),
                    List.class
            );

            specification.and(InvoiceSpecification.hasContractTermIdIn(contractTermIds));
            specification.and(InvoiceSpecification.hasRoomIdsIn(roomTermIds));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return invoiceRepo.findAll(specification, pageable);
    }

    @Override
    public Invoice createInvoice(InvoiceReqDto invoiceReqDto) {
        Invoice invoice = objectMapper.convertValue(invoiceReqDto, Invoice.class);
        invoice.setInvoiceStatus(EPaymentStatus.PENDING);

        return invoiceRepo.save(invoice);
    }
}
