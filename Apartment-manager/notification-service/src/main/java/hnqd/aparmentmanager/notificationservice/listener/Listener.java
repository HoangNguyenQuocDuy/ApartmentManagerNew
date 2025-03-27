package hnqd.aparmentmanager.notificationservice.listener;

import com.rabbitmq.client.Channel;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.notificationservice.service.IEmailProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Listener {
    private final IEmailProvider emailProvider;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "notificationQueue")
    public void handleNotification(Map<String, String> otpMessage, Message message, Channel channel) {
        handleSendMail(otpMessage, message, channel);
    }

    @RabbitListener(queues = "dlq")
    public void handleDeadLetterNotification(Map<String, String> otpMessage, Message message, Channel channel) {
        log.info("Notification dead letter ---------------------->>>>>>>>>>");
        handleSendMail(otpMessage, message, channel);
    }

    @RabbitListener(queues = "chatQueue")
    public void handleChatMessage(ChatMessageRequestDto chatMessage) {
        messagingTemplate.convertAndSend("/chat-rooms", chatMessage);
    }

    @RabbitListener(queues = "alertQueue")
    public void handleAlert(Map<String, String> data, Message message, Channel channel) {
        handleSendMail(data, message, channel);
    }

    private void handleSendMail(Map<String, String> paramMessage, Message message, Channel channel) {
        try {
            log.info("Received notification message: {}", paramMessage);
            String email = paramMessage.get("email");
            String mailType = paramMessage.get("mailType");
            String subject = paramMessage.get("subject");

            emailProvider.sendEmail(email, subject, mailType, paramMessage);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Error while processing notification message: {}", e.getMessage());

            // sending mail failed -> direct to DLQ
            try {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); // true: requeue (quay lại DLQ)
            } catch (IOException ex) {
                log.error("Error when rejecting message to DLQ: {}", ex.getMessage());
            }
        }
    }
}
