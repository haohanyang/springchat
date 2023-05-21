package haohanyang.springchat.services;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import haohanyang.springchat.dtos.RegistrationForm;
import haohanyang.springchat.identity.ApplicationUserDetails;
import haohanyang.springchat.models.Group;
import haohanyang.springchat.models.Membership;
import haohanyang.springchat.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import haohanyang.springchat.dtos.UserDto;
import haohanyang.springchat.repositories.MembershipRepository;
import haohanyang.springchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserManager implements UserDetailsManager {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserManager(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void createUser(UserDetails user) {
        var username = user.getUsername();
        var password = user.getPassword();

        if (user instanceof ApplicationUserDetails applicationUserDetails) {
            var firstName = applicationUserDetails.getFirstName();
            var lastName = applicationUserDetails.getLastName();
            var email = applicationUserDetails.getEmail();
            Assert.isTrue(userRepository.findByUsername(username).isEmpty(), "Username " + username + " already exists");
            userRepository.save(new User(username, passwordEncoder.encode(password), email, firstName, lastName));
        } else {
            throw new IllegalArgumentException("The argument user must be an instance of ApplicationUserDetails");
        }

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        }
        userRepository.delete(user.get());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        var user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        return new ApplicationUserDetails(user.get());
    }


}
