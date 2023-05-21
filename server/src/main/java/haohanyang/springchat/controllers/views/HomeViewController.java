package haohanyang.springchat.controllers.views;

import haohanyang.springchat.identity.ApplicationUserDetails;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeViewController {
    Logger logger = org.slf4j.LoggerFactory.getLogger(HomeViewController.class);

    @GetMapping("/")
    public String index(Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return "index";
    }
}
