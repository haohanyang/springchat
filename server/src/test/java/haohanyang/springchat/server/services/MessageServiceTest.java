package haohanyang.springchat.server.services;

import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @Test
    public void test_send_valid_user_message() throws Exception {
        messageService.sendUserMessage(new ChatMessage(ChatMessageType.USER, "test message", "user1", "user2"));
    }

    @Test
    public void test_send_valid_group_message() throws Exception {
        messageService.sendGroupMessage(new ChatMessage(ChatMessageType.USER, "test message", "user1", "group1"));
    }

    @Test
    public void test_user_send_non_existing_group() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> messageService.sendGroupMessage(new ChatMessage(ChatMessageType.USER, "test message", "user1", "non_existing_group")));
    }

    @Test
    public void test_non_existing_user_send_group() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> messageService.sendGroupMessage(new ChatMessage(ChatMessageType.USER, "test message", "non_existing_user", "group1")));
    }

    @Test
    public void test_user_send_non_joined_group() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> messageService.sendGroupMessage(new ChatMessage(ChatMessageType.USER, "test message", "user1", "group3")));
    }

}