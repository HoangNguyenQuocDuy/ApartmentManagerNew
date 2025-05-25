package hnqd.aparmentmanager.documentservice.config;

import hnqd.aparmentmanager.common.Enum.EKey;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtProvider {

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List getRolesFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(EKey.SECRET_KEY.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody()
                .get("authorities", List.class);
    }
}
