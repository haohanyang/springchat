package haohanyang.springchat.server.services;

import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendGroupNotification(String groupId, ChatNotification notification) {
        simpMessagingTemplate.convertAndSend("/notify/group/" + groupId, notification);
    }

    public void sendUserMessage(String username, ChatMessage message) {
        simpMessagingTemplate.convertAndSend("/receive/user/" + username, message);
    }

    public void sendGroupMessage(String groupname, ChatMessage message) {
        simpMessagingTemplate.convertAndSend("/receive/group/" + groupname, message);
    }

    public void sendUserNotification(String username, ChatNotification notification) {
        simpMessagingTemplate.convertAndSend("/notify/user/" + username, notification);
    }
}
