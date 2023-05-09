package haohanyang.springchat.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import haohanyang.springchat.dtos.UserDTO;
import haohanyang.springchat.repositories.GroupRepository;
import haohanyang.springchat.repositories.MembershipRepository;
import haohanyang.springchat.repositories.UserRepository;

public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserService(MembershipRepository membershipRepository, UserRepository userRepository,
            GroupRepository groupRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<UserDTO> getAllUsers() {
        return null;
    }

}
