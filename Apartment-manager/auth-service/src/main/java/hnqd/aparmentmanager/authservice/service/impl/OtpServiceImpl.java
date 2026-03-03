package hnqd.aparmentmanager.authservice.service.impl;

import hnqd.aparmentmanager.authservice.service.IOtpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements IOtpService {
    private final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String generateOtp(String username) {
        logger.info("#OtpServiceImpl.generateOtp - Start generateOtp");
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // otp include 6 characters
        logger.info("#OtpServiceImpl.generateOtp - OTP_USERNAME: {}", otp);
        stringRedisTemplate.opsForValue().set("OTP_" + username, otp, 5, TimeUnit.MINUTES);

        return otp;
    }

    @Override
    public boolean verifyOtp(String userId, String otp) {
        logger.info("#OtpServiceImpl.verifyOtp - Start - userId: {}, otp: {}", userId, otp);

        String storedOtp = stringRedisTemplate.opsForValue().get("OTP_" + userId);
        logger.info("#OtpServiceImpl.verifyOtp - storedOtp: {}", storedOtp);

        return otp.equals(storedOtp);
    }
}
