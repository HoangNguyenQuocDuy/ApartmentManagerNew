package hnqd.aparmentmanager.incidentservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FireAlertRequest {

    private String cameraId;
    private String location;
    private LocalDateTime timestamp;

}
