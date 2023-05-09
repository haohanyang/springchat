package haohanyang.springchat.dtos;

public record MessageDTO(MessageType messageType, String content, String sender, String receiver) {
}