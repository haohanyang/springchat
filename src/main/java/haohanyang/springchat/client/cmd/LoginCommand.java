package haohanyang.springchat.client.cmd;

public record LoginCommand(String username, String password) implements Command {
}
