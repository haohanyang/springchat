package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.server.services.AuthenticationService;

import haohanyang.springchat.server.services.AuthenticationServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
    public String verifyToken(@RequestParam(name = "token", defaultValue = "null") String token) {
        var result = authenticationService.verifyToken(token);
        if (result == AuthenticationServiceResult.SUCCESS) {
            return "Valid token";
        } else {
            return "Invalid token";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest form) {
        var result = authenticationService.login(form.username(), form.password());
        if (result == AuthenticationServiceResult.SUCCESS) {
            var token = authenticationService.generateToken(form.username());
            var response = new AuthenticationResponse(form.username(), token, "ok");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


