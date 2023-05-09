package haohanyang.springchat.controllers.views;

import haohanyang.springchat.dtos.RedirectMessage;
import haohanyang.springchat.dtos.RedirectMessageType;
import haohanyang.springchat.dtos.RegistrationForm;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import haohanyang.springchat.dtos.LoginForm;
import haohanyang.springchat.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthViewController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthViewController.class);
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthViewController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("form", new LoginForm());
        return "login";
    }

    // @PostMapping("/login")
    // public String loginPost(Model model, @Valid @ModelAttribute("form") LoginForm
    // form, BindingResult result,
    // HttpServletResponse response, RedirectAttributes redirectAttributes) {
    // if (result.hasErrors()) {
    // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    // logger.info("Invalid form");
    // return "login";
    // }

    // logger.info("Valid form");
    // try {
    // authenticationService.login(form.getUsername(), form.getPassword());
    // } catch (AuthenticationException e) {
    // // Invalid credentials
    // model.addAttribute("error", e.getMessage());
    // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // return "login";
    // } catch (Exception e) {
    // // Other errors
    // model.addAttribute("error", "Unknown error occurred");
    // logger.error("Unknown error occurred when user {} tried to log in: {}",
    // form.getUsername(), e.getMessage());
    // return "login";
    // }

    // logger.info("User {} logged in", form.getUsername());
    // redirectAttributes.addFlashAttribute("redirectMessage",
    // new RedirectMessage(RedirectMessageType.SUCCESS, "Login succeeded!"));
    // return "redirect:/";
    // }

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(Model model, @Valid @ModelAttribute("form") RegistrationForm form, BindingResult result,
            HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("Invalid form");
            return "register";
        }

        logger.info("Valid form");
        try {
            authenticationService.register(form);
        } catch (IllegalArgumentException e) {
            // Username or email already exists
            model.addAttribute("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "register";
        } catch (Exception e) {
            // Other errors
            model.addAttribute("error", "Unknown error occurred");
            logger.error("Unknown error occurred when user {} tried to register: {}", form.getUsername(),
                    e.getMessage());
            return "register";
        }
        logger.info("User {} registered", form.getUsername());
        redirectAttributes.addFlashAttribute(
                new RedirectMessage(RedirectMessageType.SUCCESS, "The account was successfully created!"));
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutPost() {
        return "logout";
    }
}
