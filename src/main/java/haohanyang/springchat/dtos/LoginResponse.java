package haohanyang.springchat.dtos;

import java.util.Objects;

public record LoginResponse(String username, String token, String message) {
    public LoginResponse {
        Objects.requireNonNull(username);
        Objects.requireNonNull(token);
    }
}
