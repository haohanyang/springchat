package haohanyang.springchat.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsManager userDetailsManager;
    private final AuthenticationTokenService authenticationTokenService;

    private static final List<RequestMatcher> noFilterRequestMatchers = List.of(
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/register"),
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/api/verify"),
            new AntPathRequestMatcher("/api/login"),
            new AntPathRequestMatcher("/api/register"),
            new AntPathRequestMatcher("/chat/**")
    );

    @Autowired
    public JwtFilter(UserDetailsManager userDetailsManager, AuthenticationTokenService authenticationTokenService) {
        this.userDetailsManager = userDetailsManager;
        this.authenticationTokenService = authenticationTokenService;
    }

    public static boolean hasValidHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return false;
        }
        if (authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    // Authenticate http request that has valid jwt
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("filter " + request.getRequestURI());
        if (hasValidHeader(request)) {
            var token = request.getHeader("Authorization").split(" ")[1].trim();
            if (!token.isBlank()) {
                String username;
                try {
                    username = authenticationTokenService.verifyToken(token);
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        try {
                            var user = userDetailsManager.loadUserByUsername(username);
                            var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                                    user.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } catch (UsernameNotFoundException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid u/" + username);
                        }
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return noFilterRequestMatchers.stream().anyMatch(e -> e.matches(request));
    }
}
