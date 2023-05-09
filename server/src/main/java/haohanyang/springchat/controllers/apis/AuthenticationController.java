package haohanyang.springchat.controllers.apis;

import haohanyang.springchat.dtos.LoginForm;
import haohanyang.springchat.dtos.LoginResponse;
import haohanyang.springchat.dtos.RegistrationForm;
import haohanyang.springchat.dtos.RegistrationRequest;
import haohanyang.springchat.services.AuthenticationService;

import haohanyang.springchat.services.AuthenticationTokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;
    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationController(AuthenticationService authenticationService,
            AuthenticationTokenService authenticationTokenService) {
        this.authenticationService = authenticationService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationForm form, BindingResult result) {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid form");
            }
            authenticationService.register(form);
            return new ResponseEntity<>("ok", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown error occurred when user {} tried to register: {}", form.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occurred");
        }
    }

    @GetMapping("/api/verify")
    public ResponseEntity<String> verifyToken(@RequestParam(name = "token", defaultValue = "") String token) {
        if (token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token:Token is empty");
        }
        try {
            authenticationTokenService.verifyToken(token);
            return ResponseEntity.ok("Valid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token:" + e.getMessage());
        }
    }

    @GetMapping("/api/user")
    public ResponseEntity<String> user() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        return ResponseEntity.ok(username);
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginForm form) {
        try {
            authenticationService.login(form.getUsername(), form.getPassword());
            var token = authenticationTokenService.generateToken(form.getUsername());
            var response = new LoginResponse(form.getUsername(), token, "ok");
            return ResponseEntity.ok().body(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(form.getUsername(), "", e.getMessage()));
        }
        catch (Exception e) {
            logger.error("Unknown error occurred when user {} tried to log in: {}", form.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(form.getUsername(), "", "Unknown error occurred"));
        }
    }

}
