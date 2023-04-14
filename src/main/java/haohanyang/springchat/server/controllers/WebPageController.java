package haohanyang.springchat.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {
    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("place", "springchat");
        return "index";
    }

}
