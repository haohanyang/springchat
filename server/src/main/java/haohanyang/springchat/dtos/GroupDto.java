package haohanyang.springchat.dtos;


import java.util.Set;

public record GroupDto(Integer id, String name, String avatarUrl, UserDto creator, Set<UserDto> members) {
}