package haohanyang.springchat.client.cmd;

import haohanyang.springchat.common.ChatMessageType;

public record SendCommand(ChatMessageType chatMessageType, String receiver, String content) implements Command {
}
