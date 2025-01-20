package hnqd.aparmentmanager.notificationservice.service.impl;

import com.rabbitmq.client.Channel;
import hnqd.aparmentmanager.notificationservice.service.IEmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class NotificationService {
    private final IEmailService emailService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationService(IEmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "notificationQueue")
    public void handleNotification(Map<String, String> otpMessage, Message message, Channel channel) {
        handleSendMail(otpMessage, message, channel);
    }

    @RabbitListener(queues = "dlq")
    public void handleDeadLetterNotification(Map<String, String> otpMessage, Message message, Channel channel) {
        log.info("Notification dead letter ---------------------->>>>>>>>>>");
        handleSendMail(otpMessage, message, channel);
    }

    private void handleSendMail(Map<String, String> otpMessage, Message message, Channel channel) {
        try {
            log.info("Received notification message: {}", otpMessage);
            String email = otpMessage.get("email");
            String otp = otpMessage.get("otp");
            String username = otpMessage.get("username");

            emailService.sendEmailWelcome(email, username, otp);

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
