package hnqd.aparmentmanager.authservice.service;


import hnqd.aparmentmanager.authservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
    String generateToken(User userDetails);
}
