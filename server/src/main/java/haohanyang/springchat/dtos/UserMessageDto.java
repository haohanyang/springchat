package haohanyang.springchat.dtos;

import java.sql.Date;

public record UserMessageDto(
        Integer id, String content, UserDto sender, UserDto receiver, String createdAt) {
}