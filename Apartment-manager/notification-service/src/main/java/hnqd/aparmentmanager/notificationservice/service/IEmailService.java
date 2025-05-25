package hnqd.aparmentmanager.notificationservice.service;

public interface IEmailService {
    void sendEmailWelcome(String to, String username, String otp);
}
