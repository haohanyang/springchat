package haohanyang.springchat.server.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsManager userDetailsManager;
    private final AuthenticationTokenService authenticationTokenService;

    // This filter doesn't filter websocket requests
    private static final RequestMatcher urlMatcher = new AntPathRequestMatcher("/chat/**");

    @Autowired
    public JwtFilter(UserDetailsManager userDetailsManager, AuthenticationTokenService authenticationTokenService) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationTokenService = authenticationTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.split(" ")[1].trim();
            if (!token.isBlank()) {
                String username = authenticationTokenService.verifyToken(token);
                if (username == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid jwt token");
                } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        var user = userDetailsManager.loadUserByUsername(username);
                        var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                                user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } catch (UsernameNotFoundException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid username");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        RequestMatcher matcher = new NegatedRequestMatcher(urlMatcher);
        return !matcher.matches(request);
    }
}
