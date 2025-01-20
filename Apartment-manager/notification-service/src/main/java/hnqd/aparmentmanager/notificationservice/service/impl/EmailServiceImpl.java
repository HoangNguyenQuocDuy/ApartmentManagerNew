package hnqd.aparmentmanager.notificationservice.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import hnqd.aparmentmanager.notificationservice.service.IEmailService;

import java.util.logging.Logger;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmailWelcome(String to, String username, String otp) {
        try {
            // Tạo MimeMessage
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // config email
            helper.setTo(to);
            helper.setSubject("Welcome to Quốc Duy Apartment");
            helper.setFrom("quocduy6114@gmail.com");

            // Tạo context cho Thymeleaf
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("otp", otp);

            // Render template thành chuỗi
            String emailContent = templateEngine.process("welcome-email-template", context);

            // Thiết lập nội dung email
            helper.setText(emailContent, true); // true nghĩa là email này là HTML

            // Gửi email
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.warning("Exception while sending email: " + e.getMessage());
        }
    }
}
