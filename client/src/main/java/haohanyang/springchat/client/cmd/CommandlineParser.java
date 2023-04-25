package haohanyang.springchat.client.cmd;

import haohanyang.springchat.common.ChatMessageType;
import jakarta.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandlineParser {
    private static Pattern loginPattern = Pattern.compile("login\s+([a-zA-Z0-9-_]+)\s+(\\S+)$");

    private static Pattern registerPattern = Pattern.compile("register\s+([a-zA-Z0-9-_]+)\s+(\\S+)$");
    private static Pattern sendPattern = Pattern.compile("^send\s+([ug])/([a-zA-Z0-9-_]+)\s+'(.+)'$");
    private static Pattern joinPattern = Pattern.compile("^join\s+g/([a-zA-Z0-9-_]+)$");

    private static Pattern exitPattern = Pattern.compile("^([qQ]|exit|quit)$");


    @Nullable
    public static Command parse(String commandString) {
        Matcher matcher;
        commandString = commandString.trim();

        if (commandString.isBlank()) {
            return null;
        }

        matcher = exitPattern.matcher(commandString);
        if (matcher.matches()) {
            return new ExitCommand();
        }

        matcher = registerPattern.matcher(commandString);
        if (matcher.matches()) {
            var username = matcher.group(1);
            var password = matcher.group(2);
            return new RegistrationCommand(username, password);
        }

        matcher = loginPattern.matcher(commandString);
        if (matcher.matches()) {
            var username = matcher.group(1);
            var password = matcher.group(2);
            return new LoginCommand(username, password);
        }

        matcher = joinPattern.matcher(commandString);
        if (matcher.matches()) {
            return new JoinGroupCommand(matcher.group(1));
        }

        matcher = sendPattern.matcher(commandString);
        if (matcher.matches()) {
            ChatMessageType type;
            if (matcher.group(1).equals("u")) {
                type = ChatMessageType.USER;
            } else {
                type = ChatMessageType.GROUP;
            }
            var receiver = matcher.group(2);
            var content = matcher.group(3);
            return new SendCommand(type, receiver, content);
        }

        return null;
    }


}
