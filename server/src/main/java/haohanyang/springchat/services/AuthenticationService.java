package haohanyang.springchat.services;

import haohanyang.springchat.dtos.RegistrationForm;
import haohanyang.springchat.identity.ApplicationUserDetails;
import haohanyang.springchat.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserDetailsManager userDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final List<GrantedAuthority> grantedAuthorityList;

    @Autowired
    public AuthenticationService(UserDetailsManager userDetailsManager, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.grantedAuthorityList = List.of(new SimpleGrantedAuthority("USER"));
    }

    public void register(RegistrationForm form) throws IllegalArgumentException {
        var user = new UserDao(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getEmail(), form.getFirstName(), form.getLastName());
        userDetailsManager.createUser(new ApplicationUserDetails(user));
    }

    public void login(String username, String password) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password,
                this.grantedAuthorityList));
    }

}
