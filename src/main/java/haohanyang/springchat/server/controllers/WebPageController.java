package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.RegistrationDTO;
import haohanyang.springchat.server.models.User;
import haohanyang.springchat.server.repositories.UserRepository;
import haohanyang.springchat.server.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebPageController {

    @Autowired
    private UserRepository userRepository;
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
    public String _register(@ModelAttribute("registration") RegistrationDTO registrationDTO, Model model) {
        try {
            authenticationService.register(registrationDTO.getUsername(), registrationDTO.getPassword());
            model.addAttribute("register_username", registrationDTO.getUsername());
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("register_error", "u/" + registrationDTO.getUsername() + " already exists");
            return "register";
        }
    }

}
