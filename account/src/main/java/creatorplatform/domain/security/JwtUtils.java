package creatorplatform.domain.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshExpirationMs;

    public String generateJwtToken(String subject) {
        return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public boolean validateJwtToken(String token) {
        try { Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token); return true; }
        catch (JwtException e) { return false; }
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public long getRefreshExpirationMs() { return refreshExpirationMs; }
}