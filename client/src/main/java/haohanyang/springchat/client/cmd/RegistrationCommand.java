package haohanyang.springchat.client.cmd;

public record RegistrationCommand(String username, String password) implements Command {
}
