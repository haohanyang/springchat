package haohanyang.springchat.controllers.apis;

import haohanyang.springchat.dtos.UserDto;
import haohanyang.springchat.identity.ApplicationUserDetails;
import haohanyang.springchat.services.UserManager;
import haohanyang.springchat.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public Set<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal ApplicationUserDetails user) {
        return user.toUserDto();
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            var user = userService.getUser(username);
            return ResponseEntity.ok().body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to get user {} with unexpected error:{}", username, e.getMessage());
            return ResponseEntity.status(500).body("Unknown error occurred");
        }
    }
}
