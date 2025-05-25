package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.ParkingRightRequest;
import hnqd.aparmentmanager.accessservice.entity.ParkingRight;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IParkingRightService {

    RestResponse<ListResponse<ParkingRight>> getParkingRightsPaging(Map<String, String> params);

    ParkingRight createParkingRight(ParkingRightRequest pr);

    ParkingRight updateParkingRight(int prId, Map<String, String> params);

    void deleteParkingRight(int prId);

}
