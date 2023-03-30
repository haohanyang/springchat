package haohanyang.springchat.common;

public record ChatMessage(ChatMessageType chatMessageType, String content, String sender, String receiver,
                          String time) {
}