package hnqd.aparmentmanager.notificationservice.service;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface IEmailProvider {

    void sendEmail(String to, String subject, String templateName, Map<String, String> data) throws MessagingException;

}
