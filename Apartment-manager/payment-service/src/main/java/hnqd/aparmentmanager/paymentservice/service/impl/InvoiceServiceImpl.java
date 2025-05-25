package hnqd.aparmentmanager.paymentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.Enum.EInvoiceType;
import hnqd.aparmentmanager.common.Enum.EPaymentStatus;
import hnqd.aparmentmanager.common.dto.request.InvoiceReqDto;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.paymentservice.client.IDocumentServiceClient;
import hnqd.aparmentmanager.paymentservice.client.IUserServiceClient;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

        Specification<Invoice> specification = Specification.where(null);
//                .where(InvoiceSpecification.hasInvoiceStatus(invoiceStatus))
//                .and(InvoiceSpecification.hasInvoiceType(invoiceType))
//                .and(InvoiceSpecification.hasCreatedAtBetween(fromDate, toDate));

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

            if (!contractTermIds.isEmpty()) {
                specification = specification.and(InvoiceSpecification.hasContractTermIdIn(contractTermIds));
            }

            if (!roomTermIds.isEmpty()) {
                specification = specification.and(InvoiceSpecification.hasRoomIdsIn(roomTermIds));
            }

            System.out.println("Final Specification: " + specification);
            System.out.println("ContractTermIds: " + contractTermIds);
            System.out.println("RoomTermIds: " + roomTermIds);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return invoiceRepo.findAll(specification, pageable);
    }

    @Override
    public Invoice createInvoice(Map<String, String> invoiceReq) {
        if (Integer.parseInt(invoiceReq.get("amount")) <= 0) {
            throw new CommonException.RequestBodyInvalid("Amount <= 0!!!");
        }
        Invoice invoice = new Invoice();

        if (invoiceReq.get("dueDate") == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 5);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 0);

            invoice.setDueDate(new Timestamp(new Date(calendar.getTimeInMillis()).getTime()).toLocalDateTime());
        } else {
            String dueDateWithTime = invoiceReq.get("dueDate") + " 23:59:59";  // Thêm giờ phút giây mặc định
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dueDateWithTime, formatter);

            if (date.isBefore(LocalDateTime.now()) || date.isEqual(LocalDateTime.now())) {
                throw new CommonException.DueDateException("Due date cannot be in the past or equal to the current date/time.");
            } else {
                invoice.setDueDate(date);
            }
        }


        invoice.setDescription(invoiceReq.get("description"));
        invoice.setAmount(BigDecimal.valueOf(Long.parseLong(invoiceReq.get("amount"))));
        invoice.setInvoiceStatus(EPaymentStatus.UNPAID);
        invoice.setInvoiceType(EInvoiceType.safeValueOfName(invoiceReq.get("invoiceType")));
        invoice.setRoomId(Integer.parseInt(invoiceReq.get("roomId")));
//        invoice.setContractTermId(invoiceReq.getC);

        return invoiceRepo.save(invoice);
    }

    @Override
    public Invoice createInvoice(InvoiceReqDto invoiceReq) {
        if (invoiceReq.getAmount() == null || invoiceReq.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException.RequestBodyInvalid("Amount must be greater than 0!");
        }

        Invoice invoice = new Invoice();
        invoice.setAmount(invoiceReq.getAmount());
        invoice.setDescription(invoiceReq.getDescription() != null ? invoiceReq.getDescription() : "");
        invoice.setInvoiceType(invoiceReq.getInvoiceType());
        invoice.setRoomId(invoiceReq.getRoomId());
        invoice.setInvoiceStatus(EPaymentStatus.UNPAID);

        // Handle due date
        if (invoiceReq.getDueDate() == null) {
            LocalDateTime defaultDueDate = LocalDateTime.now().plusDays(5)
                    .withHour(23).withMinute(59).withSecond(59);
            invoice.setDueDate(defaultDueDate);
        } else {
            if (invoiceReq.getDueDate().isBefore(LocalDateTime.now().plusSeconds(1))) {
                throw new CommonException.DueDateException("Due date cannot be in the past or equal to now.");
            }
            invoice.setDueDate(invoiceReq.getDueDate());
        }

        return invoiceRepo.save(invoice);
    }


    @Override
    public Invoice updateInvoice(int invoiceId, Map<String, String> params) {
        Invoice invoiceSave = invoiceRepo.findById(invoiceId).orElseThrow(
                () -> new CommonException.NotFoundException("Invoice not found!")
        );

        boolean check = false;

        if (params.get("status") != null && !params.get("status").isEmpty()) {
            invoiceSave.setInvoiceStatus(EPaymentStatus.safeValueOfName(params.get("status")));
            check = true;
        }
        if (params.get("invoiceType") != null && !params.get("invoiceType").isEmpty()) {
            invoiceSave.setInvoiceType(EInvoiceType.safeValueOfName(params.get("invoiceType")));
            check = true;
        }
        if (params.get("description") != null && !params.get("description").isEmpty()) {
            invoiceSave.setDescription(params.get("description"));
            check = true;
        }
        if (params.get("amount") != null && !params.get("amount").isEmpty()) {
            invoiceSave.setAmount(new BigDecimal(params.get("amount")));
            check = true;
        }
        if (params.get("dueDate") != null && !params.get("dueDate").isEmpty()) {
            String dueDateWithTime = params.get("dueDate") + " 23:59:59";  // Thêm giờ phút giây mặc định
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dueDateWithTime, formatter);
            invoiceSave.setDueDate(date);
            check = true;
        }

        if (check) {
            return invoiceRepo.save(invoiceSave);
        } else {
            throw new CommonException.RequestBodyInvalid("No keys match with invoice column");
        }
    }
}
