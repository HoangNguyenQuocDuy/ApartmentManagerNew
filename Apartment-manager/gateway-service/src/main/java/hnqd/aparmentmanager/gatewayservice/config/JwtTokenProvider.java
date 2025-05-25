package hnqd.aparmentmanager.gatewayservice.config;

import hnqd.aparmentmanager.common.Enum.EKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${secret.key}")
    private String secretKey;

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().toInstant().isAfter(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Integer.class);
    }
}
