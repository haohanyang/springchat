package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.server.services.AuthenticationService;

import haohanyang.springchat.server.services.AuthenticationServiceResult;
import haohanyang.springchat.server.services.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationTokenService authenticationTokenService) {
        this.authenticationService = authenticationService;
        this.authenticationTokenService = authenticationTokenService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthenticationRequest form) {
        var result = authenticationService.register(form.username(), form.password());
        if (result == AuthenticationServiceResult.USER_EXISTS) {
            return new ResponseEntity<>("User " + form.username() + " already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Registration succeeds", HttpStatus.CREATED);
    }


    @GetMapping("/verify")
    public String verifyToken(@RequestParam(name = "username", defaultValue = "") String username,
                              @RequestParam(name = "token", defaultValue = "") String token) {
        if (token.isBlank()) {
            return "Invalid token";
        }
        var result = authenticationTokenService.verifyToken(token);
        if (result == null) {
            return "Invalid token";
        }

        if (username.isEmpty()) {
            return "Valid token";
        }
        if (result.equals(username)) {
            return "Valid token and username";
        } else {
            return "Invalid token, but not from user" + username;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest form) {
        var result = authenticationService.login(form.username(), form.password());
        if (result == AuthenticationServiceResult.SUCCESS) {
            var token = authenticationTokenService.generateToken(form.username());
            var response = new AuthenticationResponse(form.username(), token, "ok");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Test authentication
    @GetMapping("/auth")
    public String requireAuth() {
        return "You are authorized";
    }
}


