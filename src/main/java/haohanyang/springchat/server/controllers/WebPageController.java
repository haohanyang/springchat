package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.RegistrationDTO;
import haohanyang.springchat.server.services.AuthenticationService;
import haohanyang.springchat.server.services.AuthenticationServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.management.RuntimeErrorException;

@Controller
public class WebPageController {

    private final AuthenticationService authenticationService;

    @Autowired
    public WebPageController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("place", "springchat");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String _login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registration", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String _register(@ModelAttribute("registration") RegistrationDTO registrationDTO, BindingResult result, Model model) {

        var response = authenticationService.register(registrationDTO.getUsername(), registrationDTO.getPassword());
        if (response == AuthenticationServiceResult.SUCCESS) {
            model.addAttribute("register_username", registrationDTO.getUsername());
            return "index";
        }

        model.addAttribute("register_error", "Failed to register");
        return "register";
    }

}
