package haohanyang.springchat.common;


import java.util.Objects;

public record AuthenticationRequest(String username, String password) {
    public AuthenticationRequest {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
    }
}
