package hnqd.aparmentmanager.incidentservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.incidentservice.dto.FireAlertRequest;
import hnqd.aparmentmanager.incidentservice.model.FireAlert;
import hnqd.aparmentmanager.incidentservice.repository.FireAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FireAlertListener {
    private final FireAlertRepository fireAlertRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "alertQueue")
    public void handleFireAlert(Map<String, String> message) {
        try {
            String jsonString = objectMapper.writeValueAsString(message);

            FireAlertRequest alertRequest = objectMapper.readValue(jsonString, FireAlertRequest.class);

            FireAlert fireAlert = new FireAlert();
            fireAlert.setCameraId(alertRequest.getCameraId());
            fireAlert.setLocation(alertRequest.getLocation());
            fireAlert.setTimestamp(alertRequest.getTimestamp());

            fireAlertRepository.save(fireAlert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
