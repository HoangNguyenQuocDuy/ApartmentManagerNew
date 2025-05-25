package hnqd.aparmentmanager.visitorservice.service;

import hnqd.aparmentmanager.visitorservice.dto.request.AccessRequestDto;
import hnqd.aparmentmanager.visitorservice.entity.SecurityLog;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ISecurityLogService {

    SecurityLog checkinVisitor(AccessRequestDto accessRequestDto);

    SecurityLog checkoutVisitor(AccessRequestDto accessRequestDto);

    Page<SecurityLog> getSecurityLogs(Map<String, String> params);

}
