package hnqd.aparmentmanager.accessservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.accessservice.client.IDocumentServiceClient;
import hnqd.aparmentmanager.accessservice.dto.ParkingRightRequest;
import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import hnqd.aparmentmanager.accessservice.repository.IParkingRightRepository;
import hnqd.aparmentmanager.accessservice.repository.IRelativeRepository;
import hnqd.aparmentmanager.accessservice.service.IParkingRightService;
import hnqd.aparmentmanager.accessservice.specification.ParkingRightSpecification;
import hnqd.aparmentmanager.common.Enum.ECardStatus;
import hnqd.aparmentmanager.common.exceptions.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParkingRightServiceImpl implements IParkingRightService {

    private final IParkingRightRepository parkingRightRepository;
    private final IRelativeRepository relativeRepository;
    private final ObjectMapper objectMapper;
    private final IDocumentServiceClient documentServiceClient;

    @Autowired
    public ParkingRightServiceImpl(
            IParkingRightRepository parkingRightRepository,
            IRelativeRepository relativeRepository,
            ObjectMapper objectMapper,
            IDocumentServiceClient documentServiceClient
    ) {
        this.parkingRightRepository = parkingRightRepository;
        this.relativeRepository = relativeRepository;
        this.objectMapper = objectMapper;
        this.documentServiceClient = documentServiceClient;
    }

    @Override
    public Page<ParkingRight> getParkingRightsPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("size", "20"));
        int userId = Integer.parseInt(params.getOrDefault("userId", "0"));
        int roomId = Integer.parseInt(params.getOrDefault("roomId", "0"));
        String cardStatus = params.getOrDefault("cardStatus", null);

        List<Integer> contractIds = new ArrayList<>();
        Specification<ParkingRight> spec = Specification.where(null);

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
        if (cardStatus != null) {
            spec.and(ParkingRightSpecification.hasParkingRightStatus(cardStatus));
        }
        if (!contractIds.isEmpty()) {
            spec.and(ParkingRightSpecification.hasRelativeInContractId(contractIds));
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        return parkingRightRepository.findAll(spec, pageable);
    }

    @Override
    public ParkingRight createParkingRight(ParkingRightRequest pr) {
        ParkingRight parkingRight = ParkingRight
                .builder()
                .status(ECardStatus.Active)
                .typeOfVehicle(pr.getTypeOfVehicle())
                .licensePlates(pr.getLicensePlate())
                .relative(relativeRepository.findById(pr.getRelativeId()).orElseThrow(() ->
                    new CommonException.NotFoundException("Relative with id " + pr.getRelativeId() + " not found")
                ))
                .build();

        return parkingRightRepository.save(parkingRight);
    }

    @Override
    public ParkingRight updateParkingRight(int prId, Map<String, String> params) {
        ParkingRight parkingRight = parkingRightRepository.findById(prId).orElseThrow(() ->
                new CommonException.NotFoundException("Parking right with id " + prId + " not found")
        );

        String cardStatus = params.getOrDefault("cardStatus", "");

        if (!cardStatus.isEmpty() && parkingRight.getStatus() == ECardStatus.Pending) {
            parkingRight.setStatus(ECardStatus.valueOf(cardStatus));

            return parkingRightRepository.save(parkingRight);
        }

        return null;
    }

    @Override
    public void deleteParkingRight(int prId) {
        ParkingRight parkingRight = parkingRightRepository.findById(prId).orElseThrow(() ->
                new CommonException.NotFoundException("Parking right with id " + prId + " not found")
        );

        parkingRightRepository.delete(parkingRight);
    }
}
