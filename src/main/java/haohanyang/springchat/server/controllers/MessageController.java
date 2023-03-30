package haohanyang.springchat.server.controllers;


import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import haohanyang.springchat.common.ChatNotification;
import haohanyang.springchat.common.ChatNotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/user")
    public void userMessage(@Payload ChatMessage chatMessage) throws Exception {
        Thread.sleep(500);
        var content = chatMessage.content();
        var sender = chatMessage.sender();
        var receiver = chatMessage.receiver();
        logger.info(sender + " -> " + "user/" + receiver + ":" + content);
        var message = new ChatMessage(chatMessage.chatMessageType(), content, sender,
                receiver, LocalDateTime.now().toString());
        simpMessagingTemplate.convertAndSend("/receive/user/" + receiver, message);

        var notification = new ChatNotification(ChatNotificationType.SUCCESS, "Message sent");
        simpMessagingTemplate.convertAndSend("/notify/user/" + sender, notification);
    }

    @MessageMapping("/group")
    public void groupMessage(@Payload ChatMessage chatMessage) throws Exception {
        Thread.sleep(500);
        var content = chatMessage.content();
        var sender = chatMessage.sender();
        var receiver = chatMessage.receiver();
        logger.info(sender + " -> " + "group/" + receiver + ":" + content);
        var m = new ChatMessage(chatMessage.chatMessageType(), content, sender,
                receiver, LocalDateTime.now().toString());
        simpMessagingTemplate.convertAndSend("/receive/group/" + receiver, m);
    }

    @PostMapping("/notify")
    public String sendNotification(@RequestBody ChatNotification notification, @RequestParam String username) {
        simpMessagingTemplate.convertAndSend("/receive/user/" + username, notification);
        return "ok";
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage chatMessage) {
        try {
            Thread.sleep(500);
            var content = chatMessage.content();
            var sender = chatMessage.sender();
            var receiver = chatMessage.receiver();
            logger.info(sender + " -> " + "user/" + receiver + ":" + content);
            var m = new ChatMessage(chatMessage.chatMessageType(), content, sender,
                    receiver, LocalDateTime.now().toString());
            if (chatMessage.chatMessageType() == ChatMessageType.USER) {
                simpMessagingTemplate.convertAndSend("/receive/user/" + receiver, m);
            } else {
                simpMessagingTemplate.convertAndSend("/receive/group/" + receiver, m);
            }
            return ResponseEntity.ok().body("ok");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

}
