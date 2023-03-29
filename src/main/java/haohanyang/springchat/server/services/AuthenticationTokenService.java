package haohanyang.springchat.server.services;

import haohanyang.springchat.server.controllers.MessageController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationTokenService {

    Logger logger = LoggerFactory.getLogger(AuthenticationTokenService.class);
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    private String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateToken(String username) {
        var token = Jwts.builder().setSubject(username)
                .setIssuer("springchat")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256,
                        Decoders.BASE64.decode(SECRET_KEY))
                .compact();
        return token;
    }

    // Verify the token, return claimed username if it is valid.
    // Return null otherwise.
    @Nullable
    public String verifyToken(String token) {
        try {
            var parser = Jwts.parserBuilder().setSigningKey(Decoders.BASE64URL.decode(SECRET_KEY)).build();
            var body = parser.parseClaimsJws(token.trim()).getBody();
            var username = (String) body.get("sub");
            logger.info("Verification of token " + token + " succeeds");
            return username;
        } catch (Exception e) {
            logger.info("Verification of token " + token + " fails:" + e.getMessage());
            return null;
        }
    }
}
