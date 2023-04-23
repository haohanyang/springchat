package haohanyang.springchat.server.services;


import haohanyang.springchat.common.ChatNotification;
import haohanyang.springchat.common.ChatNotificationType;
import haohanyang.springchat.server.models.Membership;
import haohanyang.springchat.server.repositories.GroupRepository;
import haohanyang.springchat.server.repositories.MembershipRepository;
import haohanyang.springchat.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    Logger logger = LoggerFactory.getLogger(UserGroupService.class);

    private final Map<String, Set<String>> userGroups = new HashMap<>();
    private final Map<String, Set<String>> groupMembers = new HashMap<>();
    private final Lock mutex = new ReentrantLock();

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserGroupService(MembershipRepository membershipRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    public Map<String, Set<String>> getUserGroups() {
        try {
            mutex.lock();
            return userGroups;
        } finally {
            mutex.unlock();
        }
    }

    public Set<String> getUserGroups(String username) {
        try {
            mutex.lock();
            var groups = userGroups.get(username);
            if (groups != null) {
                return groups;
            }
            return Set.of();
        } finally {
            mutex.unlock();
        }
    }

    public Map<String, Set<String>> getGroupMembers() {
        try {
            mutex.lock();
            return groupMembers;
        } finally {
            mutex.unlock();
        }
    }

    public void addUser(String username) {
        try {
            mutex.lock();
            userGroups.computeIfAbsent(username, k -> {
                logger.info("New user u/{} connected", username);
                return new HashSet<>();
            });
        } finally {
            mutex.unlock();
        }
    }

    public boolean userInGroup(String username, String groupId) {
        try {
            mutex.lock();
            var members = groupMembers.get(groupId);
            var groups = userGroups.get(username);
            return (members != null && groups != null && members.contains(username) && groups.contains(groupId));
        } finally {
            mutex.unlock();
        }
    }

    public void addGroup(String groupId) {
        try {
            mutex.lock();
            groupMembers.computeIfAbsent(groupId, k -> new HashSet<>());
        } finally {
            mutex.unlock();
        }
    }

    public Set<String> getGroupMembers(String groupId) {
        try {
            mutex.lock();
            var members = groupMembers.get(groupId);
            if (members != null) {
                return members;
            }
            return Set.of();
        } finally {
            mutex.unlock();
        }
    }

    @Transactional
    public void addMember(String username, String groupName) throws Exception {
        var user = userRepository.findByUsername(username);
        var group = groupRepository.findByGroupName(groupName);
        if (user.isEmpty())
            throw new IllegalArgumentException("User " + username + " doesn't exist");
        if (group.isEmpty())
            throw new IllegalArgumentException("Group " + groupName + " doesn't exist");
        var joinedGroups = user.get().getMemberships().stream().map(e -> e.getGroup().getGroupName()).collect(Collectors.toSet());
        if (joinedGroups.contains(groupName))
            throw new IllegalArgumentException("User " + username + " is already in the group " + groupName);
        var membership = new Membership(user.get(), group.get());
        membershipRepository.save(membership);
//        ChatNotification notification = null;
//        try {
//            mutex.lock();
//            var members = groupMembers.get(groupId);
//            if (members != null) {
//                if (!members.add(username)) {
//                    logger.warn("Add u/{} to g/{}:{}", username, groupId, "User is already in the group");
//                    notification = new ChatNotification(ChatNotificationType.WARNING, "You are already in g/" + groupId);
//                }
//            } else {
//                // Error:Group doesn't exist
//                logger.error("Add u/{} to g/{}:{}", username, groupId, "Group doesn't exist");
//                notification = new ChatNotification(ChatNotificationType.ERROR, "g/" + groupId + " doesn't exist");
//                return notification;
//            }
//
//            var groups = userGroups.get(username);
//            if (groups != null) {
//                if (!groups.add(groupId)) {
//                    logger.warn("Add u/{} to g/{}:{}", username, groupId, "User is already in the group");
//                    notification = new ChatNotification(ChatNotificationType.WARNING, "You are already in g/" + groupId);
//                }
//            } else {
//                // Error:User doesn't exist
//                logger.error("Add u/{} to g/{}:{}", username, groupId, "User doesn't exist");
//                notification = new ChatNotification(ChatNotificationType.ERROR, "User doesn't exist");
//                return notification;
//            }
//        } catch (Exception e) {
//            logger.error("Add u/{} to g/{}:{}", username, groupId, e.getMessage());
//            notification = new ChatNotification(ChatNotificationType.ERROR, e.getMessage());
//            return notification;
//        } finally {
//            mutex.unlock();
//        }
//
//        if (notification != null) {
//            return notification;
//        }
//        return new ChatNotification(ChatNotificationType.SUCCESS, "u/" + username + " joined the group");
    }

    public ChatNotification removeMember(String username, String groupId) {
        ChatNotification notification = null;
        try {
            mutex.lock();
            var members = groupMembers.get(groupId);
            if (members != null) {
                if (!members.remove(username)) {
                    logger.warn("Remove u/{} from g/{}:{}", username, groupId, "User is not in the group");
                    notification = new ChatNotification(ChatNotificationType.WARNING, "You are not in g/" + groupId);
                }
            } else {
                // Error: Group doesn't exist
                logger.error("Remove u/{} from g/{}:{}", username, groupId, "Group doesn't exist");
                return new ChatNotification(ChatNotificationType.ERROR, "g/" + groupId + " doesn't exist");
            }

            var groups = userGroups.get(username);
            if (groups != null) {
                if (!groups.remove(groupId)) {
                    logger.warn("Remove u/{} to g/{}:{}", username, groupId, "User is not in the group");
                    notification = new ChatNotification(ChatNotificationType.WARNING, "You are not in g/" + groupId);
                }
            } else {
                // Error: User doesn't exist
                logger.error("Remove u/{} to g/{}:{}", username, groupId, "User doesn't exist");
                return new ChatNotification(ChatNotificationType.ERROR, "User doesn't exist");
            }
        } catch (Exception e) {
            logger.error("Remove u/{} to g/{}:{}", username, groupId, e.getMessage());
            return new ChatNotification(ChatNotificationType.ERROR, e.getMessage());
        } finally {
            mutex.unlock();
        }
        if (notification == null) {
            return new ChatNotification(ChatNotificationType.SUCCESS, "ok");
        }
        return notification;
    }

}
