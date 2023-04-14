package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.server.services.AuthenticationService;

import haohanyang.springchat.server.services.AuthenticationServiceResult;
import haohanyang.springchat.server.services.AuthenticationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;
    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationTokenService authenticationTokenService) {
        this.authenticationService = authenticationService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody AuthenticationRequest form) {
        var result = authenticationService.register(form.username(), form.password());
        if (result == AuthenticationServiceResult.USER_EXISTS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + form.username() + " already exists");
        }
        return new ResponseEntity<>("Registration succeeds", HttpStatus.CREATED);
    }

    @GetMapping("/api/verify")
    public ResponseEntity<String> verifyToken(@RequestParam(name = "token", defaultValue = "") String token) {
        if (token.isBlank() || authenticationTokenService.verifyToken(token) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid");
        }
        return ResponseEntity.ok("Valid token");
    }

    @PostMapping("/api/login")
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
}


