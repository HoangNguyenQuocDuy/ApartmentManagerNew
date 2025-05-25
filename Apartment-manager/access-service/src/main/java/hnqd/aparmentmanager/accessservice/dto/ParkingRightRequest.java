package hnqd.aparmentmanager.accessservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRightRequest {

    private int relativeId;
    private String typeOfVehicle;
    private String licensePlates;

}
