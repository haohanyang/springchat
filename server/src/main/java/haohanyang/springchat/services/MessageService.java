package haohanyang.springchat.services;


import haohanyang.springchat.dtos.MessageDTO;
import haohanyang.springchat.dtos.NotificationDTO;
import haohanyang.springchat.models.GroupMessageDao;
import haohanyang.springchat.models.UserMessageDao;
import haohanyang.springchat.repositories.GroupMessageRepository;
import haohanyang.springchat.repositories.GroupRepository;
import haohanyang.springchat.repositories.UserMessageRepository;
import haohanyang.springchat.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public MessageService(SimpMessagingTemplate simpMessagingTemplate, GroupMessageRepository groupMessageRepository,
            UserMessageRepository userMessageRepository, UserRepository userRepository,
            GroupRepository groupRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.groupMessageRepository = groupMessageRepository;
        this.userMessageRepository = userMessageRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public void sendGroupNotification(String groupId, NotificationDTO notification) {
        simpMessagingTemplate.convertAndSend("/notify/group/" + groupId, notification);
    }

    @Transactional
    public void sendUserMessage(MessageDTO message) throws Exception {
        var sender = userRepository.findByUsername(message.sender());
        var receiver = userRepository.findByUsername(message.receiver());
        if (sender.isEmpty())
            throw new IllegalArgumentException("User " + message.sender() + " doesn't exist");
        if (receiver.isEmpty())
            throw new IllegalArgumentException("User " + message.receiver() + " doesn't exist");
        simpMessagingTemplate.convertAndSend("/receive/user/" + message.receiver(), message);
        userMessageRepository.save(new UserMessageDao(sender.get(), receiver.get(), message.content()));
    }

    @Transactional
    public void sendGroupMessage(MessageDTO message) {
        var sender = userRepository.findByUsername(message.sender());
        var group = groupRepository.findByGroupName(message.receiver());
        if (sender.isEmpty())
            throw new IllegalArgumentException("User " + message.sender() + " doesn't exist");
        if (group.isEmpty())
            throw new IllegalArgumentException("Group " + message.receiver() + " doesn't exist");
        if (!sender.get().isMemberOf(group.get()))
            throw new IllegalArgumentException("User " + message.sender() + " is not in group " + message.receiver());
        simpMessagingTemplate.convertAndSend("/receive/group/" + message.receiver(), message);
        groupMessageRepository.save(new GroupMessageDao(sender.get(), group.get(), message.content()));
    }

    @Transactional(readOnly = true)
    public Set<UserMessageDao> getUserMessagesSent(String username) {
        return userMessageRepository.findUserMessageBySenderUsername(username);
    }

    @Transactional(readOnly = true)
    public Set<UserMessageDao> getUserMessagesReceived(String username) {
        return userMessageRepository.findUserMessageByReceiverUsername(username);
    }

    public void sendUserNotification(String username, NotificationDTO notification) {
        simpMessagingTemplate.convertAndSend("/notify/user/" + username, notification);
    }
}
