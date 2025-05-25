package hnqd.aparmentmanager.notificationservice.service.impl;

import com.rabbitmq.client.Channel;
import hnqd.aparmentmanager.common.dto.request.ChatMessageRequestDto;
import hnqd.aparmentmanager.notificationservice.service.IEmailProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final IEmailProvider emailProvider;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpMessagingTemplate messagingTemplate;

    // Listen for notifications from the notificationQueue
    @RabbitListener(queues = "notificationQueue")
    public void handleNotification(Map<String, String> otpMessage) {
        handleSendMail(otpMessage);
    }

    // Listen for messages in the Dead Letter Queue (dlq)
//    @RabbitListener(queues = "dlq")
//    public void handleDeadLetterNotification(Map<String, String> otpMessage) {
//        log.info("Notification dead letter ---------------------->>>>>>>>>>");
//        handleSendMail(otpMessage);
//    }

    // Handle chat messages from chatQueue
    @RabbitListener(queues = "chatQueue")
    public void handleChatMessage(ChatMessageRequestDto chatMessage) {
        messagingTemplate.convertAndSend("/chat-rooms", chatMessage);
    }

    // Handle alerts from alertQueue
    @RabbitListener(queues = "alertQueue")
    public void handleAlert(Map<String, String> otpMessage) {
        handleSendMail(otpMessage);
    }

    // Centralized mail handling
    private void handleSendMail(Map<String, String> paramMessage) {
        try {
            log.info("Received notification message: {}", paramMessage);
            String email = paramMessage.get("email");
            String mailType = paramMessage.get("mailType");
            String subject = paramMessage.get("subject");

            // Send email using the provided email provider
            emailProvider.sendEmail(email, subject, mailType, paramMessage);

        } catch (Exception e) {
            log.error("Error while processing notification message: {}", e.getMessage());
            // Let Spring handle message acknowledgment and DLQ (Dead Letter Queue)
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
