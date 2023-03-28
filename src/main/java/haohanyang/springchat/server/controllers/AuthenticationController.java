package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.AuthenticationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest form) {
        return "login";
    }
}
