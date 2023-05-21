package haohanyang.springchat.controllers.views;

import haohanyang.springchat.dtos.UserDto;
import haohanyang.springchat.identity.ApplicationUserDetails;
import jakarta.annotation.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserAdvice {
    @ModelAttribute("currentUser")
    @Nullable
    public UserDto getCurrentUser(@AuthenticationPrincipal ApplicationUserDetails userDetails) {

        if (userDetails != null) {
            return userDetails.toUserDto();
        }
        System.out.println("userDetails is null!!");
        return null;
    }
}
