package haohanyang.springchat.client.cmd;

import haohanyang.springchat.common.Message;
import haohanyang.springchat.common.MessageType;

public record SendCommand(MessageType messageType, String receiver, String content) implements Command {
}
