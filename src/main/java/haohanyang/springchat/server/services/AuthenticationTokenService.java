package haohanyang.springchat.server.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationTokenService {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hours
    @Value("${secret-key}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String jwtIssuer;

    Logger logger = LoggerFactory.getLogger(AuthenticationTokenService.class);

    public String generateToken(String username) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder().setSubject(username)
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String verifyToken(String token) throws Exception {
        var parser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        var body = parser.parseClaimsJws(token.trim()).getBody();
        var username = (String) body.get("sub");
        logger.info("u/{}'s token was validated", username);
        return username;
    }
}
