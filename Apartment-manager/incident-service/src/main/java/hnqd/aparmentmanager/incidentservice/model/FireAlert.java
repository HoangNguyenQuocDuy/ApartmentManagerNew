package hnqd.aparmentmanager.incidentservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "fire_alerts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FireAlert {

    @Id
    private String id;
    private String cameraId;
    private String location;
    private LocalDateTime timestamp;

}
