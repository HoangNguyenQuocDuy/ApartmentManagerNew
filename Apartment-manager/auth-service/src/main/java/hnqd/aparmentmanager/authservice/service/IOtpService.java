package hnqd.aparmentmanager.authservice.service;

public interface IOtpService {
    String generateOtp(String username);
    boolean verifyOtp(String userId, String otp);
}
