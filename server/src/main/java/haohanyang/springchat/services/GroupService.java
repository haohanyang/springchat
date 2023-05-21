package haohanyang.springchat.services;

import haohanyang.springchat.dtos.GroupDto;
import haohanyang.springchat.models.Group;
import haohanyang.springchat.models.Membership;
import haohanyang.springchat.repositories.GroupRepository;
import haohanyang.springchat.repositories.MembershipRepository;
import haohanyang.springchat.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public GroupService(MembershipRepository membershipRepository, UserRepository userRepository,
                        GroupRepository groupRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional(readOnly = true)
    public Set<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream().map(Group::toDto).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public GroupDto getGroup(int groupId) throws IllegalArgumentException {
        var group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new IllegalArgumentException("Group " + groupId + " doesn't exist");
        return group.get().toDto();
    }

    @Transactional
    public int createGroup(String username, String groupName) throws IllegalArgumentException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        var group = new Group(user.get(), groupName);
        var membership = new Membership(user.get(), group);
        groupRepository.save(group);
        membershipRepository.save(membership);
        return group.getId();
    }


    @Transactional
    public void addMember(String username, int groupId) throws Exception {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");

        var group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new IllegalArgumentException("Group " + groupId + " doesn't exist");

        if (membershipRepository.findByMemberAndGroup(user.get(), group.get()).isPresent()) {
            throw new IllegalArgumentException("User " + username + " is already in the group " + groupId);
        }
        var membership = new Membership(user.get(), group.get());
        membershipRepository.save(membership);
    }

    @Transactional
    public void removeMember(String username, int groupId) throws Exception {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");

        var group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throw new IllegalArgumentException("Group " + groupId + " doesn't exist");

        var membership = membershipRepository.findByMemberAndGroup(user.get(), group.get());
        if (membership.isEmpty()) {
            throw new IllegalArgumentException("User " + username + " is not in the group " + groupId);
        }
        membershipRepository.delete(membership.get());
    }


}
