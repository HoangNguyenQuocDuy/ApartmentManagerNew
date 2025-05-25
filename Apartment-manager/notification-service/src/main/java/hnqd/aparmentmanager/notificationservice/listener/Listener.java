package hnqd.aparmentmanager.notificationservice.listener;

import com.rabbitmq.client.Channel;
import hnqd.aparmentmanager.common.Enum.EEmailType;
import hnqd.aparmentmanager.common.dto.NotifyToUserDto;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.common.dto.request.FireAlertRequest;
import hnqd.aparmentmanager.notificationservice.service.IEmailProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Listener {
    private final IEmailProvider emailProvider;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "notificationQueue")
    public void handleNotification(Map<String, String> otpMessage) {
        handleSendMail(otpMessage);
    }

//    @RabbitListener(queues = "dlq")
//    public void handleDeadLetterNotification(Map<String, String> otpMessage) {
//        log.info("Notification dead letter ---------------------->>>>>>>>>>");
//        handleSendMail(otpMessage);
//    }

    @RabbitListener(queues = "chatQueue.notify-service")
    public void handleChatMessage(ChatMessageRequestDto chatMessage) {
        log.info("Received chat message: {}", chatMessage);
        messagingTemplate.convertAndSend(String.format("/chat-rooms/%s", chatMessage.getChatRoomId().toString()), chatMessage);
    }

    @RabbitListener(queues = "alertQueue")
    public void handleAlert(FireAlertRequest fireAlertRequest) {
        StringBuilder sb = new StringBuilder();
        String location = fireAlertRequest.getLocation();
        String timestamp = fireAlertRequest.getTimestamp().toString();
        String cameraId = fireAlertRequest.getCameraId();

        sb.append("Alert that an fire was detected at ")
                .append(location)
                .append(" at ")
                .append(timestamp);

        Map<String, String> dataSendMail =
                Map.of(
                        "email", "2151050055duy@ou.edu.vn",
                        "mailType", EEmailType.FIRE_ALERT.getName(),
                        "subject", "🔥 CẢNH BÁO KHẨN CẤP: Phát hiện cháy tại khu vực " + location,
                        "location", location,
                        "timestamp", timestamp,
                        "cameraId", cameraId
                );

        handleSendMail(dataSendMail);
        messagingTemplate.convertAndSend("/commonNotify", sb);
    }

    @RabbitListener(queues = "commonNotifyQueue")
    public void handleMessage(NotifyToUserDto notifyToUserDto) {
        messagingTemplate.convertAndSend(
                String.format("/commonNotify/%s", notifyToUserDto.getUserId()),
                        notifyToUserDto.getMessage()
                );
    }

    private void handleSendMail(Map<String, String> paramMessage) {
        try {
            log.info("Received notification message: {}", paramMessage);
            String email = paramMessage.get("email");
            String mailType = paramMessage.get("mailType");
            String subject = paramMessage.get("subject");

            emailProvider.sendEmail(email, subject, mailType, paramMessage);

//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error while processing notification message: {}", e.getMessage());

            // sending mail failed -> direct to DLQ
//            try {
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); // true: requeue (quay lại DLQ)
//            } catch (IOException ex) {
//                log.error("Error when rejecting message to DLQ: {}", ex.getMessage());
//            }
        }
    }
}
