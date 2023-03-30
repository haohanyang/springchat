package haohanyang.springchat.server.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class UserAuthenticationConfig {
    @Bean
    public UserDetailsManager userDetailsManager() {
        var initUsername1 = "user1";
        var initPassword1 = "user1";

        var initUsername2 = "user2";
        var initPassword2 = "user2";
        var encoder = new BCryptPasswordEncoder();

        var user1 =
                User.withUsername(initUsername1)
                        .password(encoder.encode(initPassword1))
                        .roles("USER")
                        .build();

        var user2 =
                User.withUsername(initUsername2)
                        .password(encoder.encode(initPassword2))
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
