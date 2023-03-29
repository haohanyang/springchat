package haohanyang.springchat.server.services;

import haohanyang.springchat.server.controllers.MessageController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final UserDetailsManager userDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final List<GrantedAuthority> grantedAuthorityList;
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    private String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public AuthenticationService(UserDetailsManager userDetailsManager, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.grantedAuthorityList = List.of(new SimpleGrantedAuthority("USER"));
    }

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

    public AuthenticationServiceResult verifyToken(String token) {
        try {
            var parser = Jwts.parserBuilder().setSigningKey(Decoders.BASE64URL.decode(SECRET_KEY)).build();
            parser.parseClaimsJws(token);
            logger.info("Verification of token " + token + " succeeds");

            return AuthenticationServiceResult.SUCCESS;
        } catch (Exception e) {
            logger.info("Verification of token " + token + " fails:" + e.getMessage());
            return AuthenticationServiceResult.AUTH_FAILS;
        }
    }

    public AuthenticationServiceResult register(String username, String password) {
        if (userDetailsManager.userExists(username)) {
            logger.info("Register with user/" + username + " fails:username already exists");
            return AuthenticationServiceResult.USER_EXISTS;
        }
        userDetailsManager.createUser(new User(username, passwordEncoder.encode(password), this.grantedAuthorityList));
        logger.info("Register with user/" + username + " succeeds");
        return AuthenticationServiceResult.SUCCESS;
    }

    public AuthenticationServiceResult login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, this.grantedAuthorityList));
            logger.info("Login with user/" + username + " succeeds");
            return AuthenticationServiceResult.SUCCESS;
        } catch (AuthenticationException e) {
            logger.info("Login with user/" + username + " fails:" + e.getMessage());
            return AuthenticationServiceResult.AUTH_FAILS;
        }
    }

}
