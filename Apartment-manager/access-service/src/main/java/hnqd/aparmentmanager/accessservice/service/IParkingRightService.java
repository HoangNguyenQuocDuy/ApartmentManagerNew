package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.ParkingRightRequest;
import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IParkingRightService {

    Page<ParkingRight> getParkingRightsPaging(Map<String, String> params);

    ParkingRight createParkingRight(ParkingRightRequest pr);

    ParkingRight updateParkingRight(int prId, Map<String, String> params);

    void deleteParkingRight(int prId);

}
