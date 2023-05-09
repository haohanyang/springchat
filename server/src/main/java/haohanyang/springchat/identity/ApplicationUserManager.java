package haohanyang.springchat.identity;

import haohanyang.springchat.models.UserDao;
import haohanyang.springchat.repositories.UserRepository;
import haohanyang.springchat.services.AuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ApplicationUserManager implements UserDetailsManager {

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Username " + username + " doesn't exist");
        return new ApplicationUserDetails(user.get());
    }

    @Transactional
    public void createUser(UserDetails user) {
        var username = user.getUsername();
        var password = user.getPassword();

        if (user instanceof ApplicationUserDetails applicationUserDetails) {
            var firstName = applicationUserDetails.getFirstName();
            var lastName = applicationUserDetails.getLastName();
            var email = applicationUserDetails.getEmail();
            Assert.isTrue(userRepository.findByUsername(username).isEmpty(), "Username " + username + " already exists");
            Assert.isTrue(userRepository.findByEmail(email).isEmpty(), "Email " + email + " already exists");
            userRepository.save(new UserDao(username, password, email, firstName, lastName));
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
        var dbUser = userRepository.findByUsername(username);
        Assert.isTrue(dbUser.isPresent(), "Username " + username + " doesn't exist");
        userRepository.delete(dbUser.get());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
