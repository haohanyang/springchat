package haohanyang.springchat.services;


import haohanyang.springchat.dtos.UserMessageDto;
import haohanyang.springchat.dtos.NotificationDTO;
import haohanyang.springchat.models.GroupMessage;
import haohanyang.springchat.models.UserMessage;
import haohanyang.springchat.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GroupMessageRepository groupMessageRepository;
    private final UserMessageRepository userMessageRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public MessageService(
            SimpMessagingTemplate simpMessagingTemplate,
            GroupMessageRepository groupMessageRepository,
            UserMessageRepository userMessageRepository,
            UserRepository userRepository,
            GroupRepository groupRepository, MembershipRepository membershipRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.groupMessageRepository = groupMessageRepository;
        this.userMessageRepository = userMessageRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public void sendUserMessage(UserMessageDto message) throws Exception {
        var sender = userRepository.findByUsername(message.sender().username());
        if (sender.isEmpty())
            throw new IllegalArgumentException("User " + message.sender() + " doesn't exist");
        var receiver = userRepository.findByUsername(message.receiver().username());
        if (receiver.isEmpty())
            throw new IllegalArgumentException("User " + message.receiver() + " doesn't exist");
        simpMessagingTemplate.convertAndSend("/receive/user/" + message.receiver(), message);
        userMessageRepository.save(new UserMessage(sender.get(), receiver.get(), message.content()));
    }

    @Transactional
    public void sendGroupMessage(UserMessageDto message) {
        var sender = userRepository.findByUsername(message.sender().username());
        if (sender.isEmpty())
            throw new IllegalArgumentException("User " + message.sender() + " doesn't exist");

        var receiver = groupRepository.findById(message.receiver().id());
        if (receiver.isEmpty())
            throw new IllegalArgumentException("Group " + message.receiver() + " doesn't exist");
        var membership = membershipRepository.findByMemberAndGroup(sender.get(), receiver.get());
        if (membership.isEmpty()) {
            throw new IllegalArgumentException("User " + message.sender().username() + " is not a member of group " + message.receiver().id());
        }
        simpMessagingTemplate.convertAndSend("/receive/group/" + message.receiver(), message);
        groupMessageRepository.save(new GroupMessage(sender.get(), receiver.get(), message.content()));
    }

    @Transactional(readOnly = true)
    public Set<UserMessage> getUserMessagesSent(String username) {
        return userMessageRepository.findUserMessageBySenderUsername(username);
    }

    @Transactional(readOnly = true)
    public Set<UserMessage> getUserMessagesReceived(String username) {
        return userMessageRepository.findUserMessageByReceiverUsername(username);
    }

    public void sendUserNotification(String username, NotificationDTO notification) {
        simpMessagingTemplate.convertAndSend("/notify/user/" + username, notification);
    }
}
