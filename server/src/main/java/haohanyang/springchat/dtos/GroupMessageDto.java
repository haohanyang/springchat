package haohanyang.springchat.dtos;

import java.sql.Date;

public record GroupMessageDto(
        Integer id,
        String content, UserDto sender, GroupDto receiver, String createdAt) {
}