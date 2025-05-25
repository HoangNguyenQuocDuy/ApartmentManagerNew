package hnqd.aparmentmanager.accessservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.accessservice.client.IDocumentServiceClient;
import hnqd.aparmentmanager.accessservice.dto.RelativeRequest;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import hnqd.aparmentmanager.accessservice.repository.IRelativeRepository;
import hnqd.aparmentmanager.accessservice.service.IRelativeService;
import hnqd.aparmentmanager.accessservice.specification.RelativeSpecification;
import hnqd.aparmentmanager.common.Enum.EContractStatus;
import hnqd.aparmentmanager.common.Enum.ERelativeType;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.common.utils.UploadImage;
import io.github.perplexhub.rsql.RSQLJPASupport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelativeServiceImpl implements IRelativeService {

    private final IRelativeRepository relativeRepository;
    private final IDocumentServiceClient documentServiceClient;
    private final ObjectMapper objectMapper;
    private final UploadImage uploadImage;

    @Override
    public RestResponse<ListResponse<Relative>> getListRelative(Map<String, String> query) {
        int page = Integer.parseInt(query.getOrDefault("page", "0"));
        int pageSize = Integer.parseInt(query.getOrDefault("size", "20"));
        int userId = Integer.parseInt(query.getOrDefault("userId", "0"));
        int roomId = Integer.parseInt(query.getOrDefault("roomId", "0"));
        boolean all = Boolean.parseBoolean(query.getOrDefault("all", "false"));
        String filter = query.getOrDefault("filter", null);

        List<Integer> contractIds = new ArrayList<>();

        if (userId != 0) {
            contractIds.addAll(objectMapper.convertValue(
                    documentServiceClient.getContractIdsByUserId(userId).getBody().getData(),
                    List.class
            ));
        }
        if (roomId != 0) {
            contractIds.addAll(objectMapper.convertValue(
                    documentServiceClient.getContractIdsByRoomId(roomId).getBody().getData(),
                    List.class
            ));
        }

        Specification<Relative> contractSpec = contractIds.isEmpty() && userId == 0 ?
                null : RelativeSpecification.hasContractIds(contractIds);

        Specification<Relative> filterable = RSQLJPASupport.toSpecification(filter);
        Specification<Relative> finalSpec = Specification.where(contractSpec);

        if (filterable != null) {
            finalSpec = finalSpec.and(filterable);
        }
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, pageSize);
        Page<Relative> resultPage = relativeRepository.findAll(finalSpec, pageable);

        return RestResponse.ok(ListResponse.of(resultPage));
    }

    @Override
    @Transactional
    public Relative createRelative(RelativeRequest relativeRequest) throws IOException {

        Optional<Relative> relativeFindWithIdCard = relativeRepository.findByIdCard(relativeRequest.getIdCard());
        if (relativeFindWithIdCard.isPresent()) {
            throw new CommonException.DuplicationError("Id Card already exists!");
        }

        ContractResponse contractFuture = objectMapper.convertValue(
                        documentServiceClient.getContractForCreateResident(
                                relativeRequest.getUserId(), relativeRequest.getRoomId(), EContractStatus.ACTIVE
                        ).getBody().getData(),
                        ContractResponse.class
        );

        Relative relative = new Relative();
        relative.setFullName(relativeRequest.getFullName());
        relative.setRelationship(relativeRequest.getRelationship());
        relative.setContractId(contractFuture.getId());
        relative.setIdCard(relativeRequest.getIdCard());
        relative.setAvatar(uploadImage.uploadToCloudinary(relativeRequest.getFile()));


        return relativeRepository.save(relative);
    }

    @Override
    public Relative updateRelative(int relativeId, MultipartFile file, Map<String, String> params) throws IOException {
        Relative relative = relativeRepository.findById(relativeId).orElseThrow(() ->
                new CommonException.NotFoundException("Relative with id " + relativeId + " not found")
        );

        if (params.containsKey("fullName") && !params.get("fullName").equals("")) {
            relative.setFullName(params.get("fullName"));
        }
        if (params.containsKey("relationship") && !params.get("relationship").equals("")) {
            relative.setRelationship(ERelativeType.valueOf(params.get("relationship")));
        }
        if (params.containsKey("idCard") && params.get("idCard").equals("")) {
            relative.setIdCard(params.get("idCard"));
        }
        if (file != null && !file.isEmpty()) {
            relative.setAvatar(uploadImage.uploadToCloudinary(file));
        }

        return relativeRepository.save(relative);
    }

    @Override
    public void deleteRelative(int relativeId) {
        Relative relative = relativeRepository.findById(relativeId).orElseThrow(() ->
                new CommonException.NotFoundException("Relative with id " + relativeId + " not found")
        );

        relativeRepository.delete(relative);
    }

    @Override
    public RestResponse<ListResponse<Relative>> getListRelative(int page, int size, String sort, String filter, String search, boolean all) {
        Specification<Relative> sortable = RSQLJPASupport.toSort(sort);
        Specification<Relative> filterable = RSQLJPASupport.toSpecification(filter);

        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page, size);

        Page<Relative> resultPage = relativeRepository.findAll(sortable.and(filterable), pageable);

        return RestResponse.ok(ListResponse.of(resultPage));
    }
}
