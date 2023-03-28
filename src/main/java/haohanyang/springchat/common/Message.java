package haohanyang.springchat.common;

import java.time.LocalDateTime;

public record Message(MessageType messageType, String content, String sender, String receiver,LocalDateTime time) {}