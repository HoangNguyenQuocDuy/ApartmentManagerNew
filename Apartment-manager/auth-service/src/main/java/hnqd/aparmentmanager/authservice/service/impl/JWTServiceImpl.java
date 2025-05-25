package hnqd.aparmentmanager.authservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hnqd.aparmentmanager.authservice.entity.User;
import hnqd.aparmentmanager.authservice.service.IJWTService;
import hnqd.aparmentmanager.common.Enum.EKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JWTServiceImpl implements IJWTService {

    @Value("${secret.key}")
    private String secretKey;

    @Override
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("authorities", List.of(user.getRoleName()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(50 * 60)))
                .sign(Algorithm.HMAC256(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8)));
//                .sign(Algorithm.HMAC256(.getBytes()));
    }

    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }
}
