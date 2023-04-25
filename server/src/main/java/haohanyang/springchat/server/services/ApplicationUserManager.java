package haohanyang.springchat.server.services;

import haohanyang.springchat.server.identity.ApplicationUserPrincipal;
import haohanyang.springchat.server.models.User;
import haohanyang.springchat.server.repositories.UserRepository;

import haohanyang.springchat.server.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
            throw new UsernameNotFoundException("Username " + username + " not found");
        return new ApplicationUserPrincipal(user.get());
    }

    @Override
    @Transactional
    public void createUser(UserDetails user) {
        var username = user.getUsername();
        var password = user.getPassword();
        Assert.isTrue(userRepository.findByUsername(username).isEmpty(), "Username " + username + " already exists");
        userRepository.save(new User(username, password));
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
    // @Transactional
    public void changePassword(String oldPassword, String newPassword) {
//        var currentUser = securityContextHolderStrategy.getContext().getAuthentication();
//        if (currentUser == null)
//            throw new AccessDeniedException(
//                    "Can't change password as no Authentication object found in context " + "for current user.");
//        String username = currentUser.getName();
//        logger.debug("Changing password for user {}", username);
//
//        // If an authentication manager has been set, re-authenticate the user with the
//        // supplied password.
//        if (authenticationManager != null) {
//            logger.debug("Re-authenticating user {} for password change request.", username);
//            authenticationManager
//                    .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
//        } else {
//            this.logger.debug("No authentication manager set. Password won't be re-checked.");
//        }
//        var user = userRepository.findByUsername(username);
//        Assert.state(user.isPresent(), "Current user doesn't exist in database.");
//        user.get().setPassword(newPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
