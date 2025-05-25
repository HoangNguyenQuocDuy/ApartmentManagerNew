package hnqd.aparmentmanager.accessservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.accessservice.client.IDocumentServiceClient;
import hnqd.aparmentmanager.accessservice.dto.EntryRightRequest;
import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import hnqd.aparmentmanager.accessservice.repository.IEntryRightRepository;
import hnqd.aparmentmanager.accessservice.repository.IRelativeRepository;
import hnqd.aparmentmanager.accessservice.service.IEntryRightService;
import hnqd.aparmentmanager.accessservice.specification.EntryRightSpecification;
import hnqd.aparmentmanager.accessservice.specification.ParkingRightSpecification;
import hnqd.aparmentmanager.common.Enum.ECardStatus;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.ResponseObject;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EntryRightServiceImpl implements IEntryRightService {

    private final IEntryRightRepository entryRightRepository;
    private final IDocumentServiceClient documentServiceClient;
    private final ObjectMapper objectMapper;
    private final IRelativeRepository relativeRepository;

    @Override
    public RestResponse<ListResponse<EntryRight>> getEntryRightsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("size", "20"));
        int userId = Integer.parseInt(params.getOrDefault("userId", "0"));
        int roomId = Integer.parseInt(params.getOrDefault("roomId", "0"));
        String cardStatus = params.getOrDefault("cardStatus", null);
        boolean all = Boolean.parseBoolean(params.getOrDefault("all", "false"));

        List<Integer> contractIds = new ArrayList<>();
        boolean flag = false;

        if (userId != 0) {
            contractIds.addAll(objectMapper.convertValue(
                    documentServiceClient.getContractIdsByUserId(userId).getBody().getData(),
                    List.class
            ));
            flag = true;
        }
        if (roomId != 0) {
            contractIds.addAll(objectMapper.convertValue(
                    documentServiceClient.getContractIdsByRoomId(roomId).getBody().getData(),
                    List.class
            ));
            flag = true;
        }

        Specification<EntryRight> spec = contractIds.isEmpty() && userId == 0 ? null :
                EntryRightSpecification.hasRelativeInContractId(contractIds);
        if (cardStatus != null) {
            spec.and(EntryRightSpecification.hasEntryRightStatus(cardStatus));
        }

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, pageSize);
        Page<EntryRight> resultPage = entryRightRepository.findAll(spec, pageable);

        return RestResponse.ok(ListResponse.of(resultPage));
    }

    @Override
    public EntryRight createEntryRight(EntryRightRequest entryRightRequest) {
        Relative relative = relativeRepository.findById(entryRightRequest.getRelativeId()).orElseThrow(() ->
                new CommonException.NotFoundException("Relative with id " + entryRightRequest.getRelativeId() + " not found")
        );

        EntryRight entryRight = new EntryRight();
        entryRight.setRelative(relative);
        entryRight.setStatus(ECardStatus.Pending);

        return entryRightRepository.save(entryRight);
    }

    @Override
    public EntryRight updateEntryRight(int erId, Map<String, String> params) {
        EntryRight entryRight = entryRightRepository.findById(erId).orElseThrow(() ->
                new CommonException.NotFoundException("EntryRight with id " + erId + " not found")
        );

        if (params.containsKey("cardStatus") && params.get("cardStatus") != "") {
            entryRight.setStatus(ECardStatus.valueOf(params.get("cardStatus")));

            return entryRightRepository.save(entryRight);
        }

        return null;
    }

    @Override
    public void deleteEntryRight(int erId) {
        EntryRight entryRight = entryRightRepository.findById(erId).orElseThrow(() ->
                new CommonException.NotFoundException("EntryRight with id " + erId + " not found")
        );

        entryRightRepository.delete(entryRight);
    }

}
