package haohanyang.springchat.services;

import haohanyang.springchat.dtos.GroupDto;
import haohanyang.springchat.dtos.UserDto;
import haohanyang.springchat.models.Group;
import haohanyang.springchat.models.Membership;
import haohanyang.springchat.models.User;
import haohanyang.springchat.repositories.MembershipRepository;
import haohanyang.springchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public UserService(UserRepository userRepository, MembershipRepository membershipRepository) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public Set<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(User::toDto).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public UserDto getUser(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        return user.get().toDto();
    }

    @Transactional(readOnly = true)
    public Set<GroupDto> getJoinedGroups(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        var memberships = membershipRepository.findByMember(user.get());
        return memberships.stream().map(Membership::getGroup).map(Group::toDto).collect(Collectors.toSet());
    }
}
