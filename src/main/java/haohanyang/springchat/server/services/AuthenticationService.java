package haohanyang.springchat.server.services;

import haohanyang.springchat.server.controllers.MessageController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserDetailsManager userDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final List<GrantedAuthority> grantedAuthorityList;

    @Autowired
    public AuthenticationService(UserDetailsManager userDetailsManager, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.grantedAuthorityList = List.of(new SimpleGrantedAuthority("USER"));
    }

    public void register(String username, String password) throws IllegalArgumentException {
        userDetailsManager.createUser(new User(username, passwordEncoder.encode(password), this.grantedAuthorityList));
        logger.info("New account u/{} was created", username);
    }

    public void login(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password,
                this.grantedAuthorityList));
        logger.info("u/{} logged in", username);
    }

}
