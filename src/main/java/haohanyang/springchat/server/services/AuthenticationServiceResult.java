package haohanyang.springchat.server.services;

public enum AuthenticationServiceResult {
    SUCCESS,
    USER_NOT_EXISTS,
    USER_EXISTS,
    INCORRECT_PASSWORD,
    AUTH_FAILS
}
