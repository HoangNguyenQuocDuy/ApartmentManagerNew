package hnqd.aparmentmanager.notificationservice.service.impl;

import hnqd.aparmentmanager.common.Enum.EEmailType;
import hnqd.aparmentmanager.notificationservice.service.IEmailProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.logging.Logger;

@Service
@Slf4j
public class GmailEmailProvider implements IEmailProvider {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public GmailEmailProvider(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, String> data) throws MessagingException {
        // Tạo MimeMessage
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // config email
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("quocduy6114@gmail.com");

        // Tạo context cho Thymeleaf
        Context context = new Context();
        String emailContent = "";

        switch (EEmailType.safeValueOfName(templateName)) {
            case WELCOME:
                context.setVariable("username", data.get("username"));
                context.setVariable("otp", data.get("otp"));
                context.setVariable("subject", subject);

                // Render template thành chuỗi
                emailContent = templateEngine.process(EEmailType.WELCOME.getTemplateName(), context);

                // Thiết lập nội dung email
                break;
            case PAYMENT_SUCCESS:
                context.setVariable("paymentDate", data.get("paymentDate"));
                context.setVariable("amount", data.get("amount"));
                context.setVariable("transactionId", data.get("transactionId"));
                context.setVariable("subject", subject);

                emailContent = templateEngine.process(EEmailType.PAYMENT_SUCCESS.getTemplateName(), context);

        }

        helper.setText(emailContent, true); // true nghĩa là email này là HTML
        javaMailSender.send(message);
    }
}
