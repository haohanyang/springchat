package haohanyang.springchat.controllers.views;

import haohanyang.springchat.dtos.RedirectMessage;
import haohanyang.springchat.dtos.RedirectMessageType;
import haohanyang.springchat.dtos.RegistrationForm;
import haohanyang.springchat.identity.ApplicationUserDetails;
import haohanyang.springchat.models.User;
import haohanyang.springchat.services.UserManager;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import haohanyang.springchat.dtos.LoginForm;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthViewController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthViewController.class);
    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;

    @Autowired
    public AuthViewController(AuthenticationManager authenticationManager, UserManager userManager) {
        this.authenticationManager = authenticationManager;
        this.userManager = userManager;
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("form", new LoginForm());
        return "login";
    }

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
            var user = new User(form.getUsername(), form.getPassword(), form.getEmail(), form.getFirstName(),
                    form.getLastName());
            userManager.createUser(new ApplicationUserDetails(user));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "register";
        } catch (Exception e) {
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
}
