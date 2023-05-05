package haohanyang.springchat.dtos;

import java.util.Objects;

public record AuthenticationResponse(String username, String token, String message) {
    public AuthenticationResponse {
        Objects.requireNonNull(username);
        Objects.requireNonNull(token);
    }
}
