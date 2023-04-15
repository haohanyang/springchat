package haohanyang.springchat.server.controllers;


import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import haohanyang.springchat.common.ChatNotification;
import haohanyang.springchat.server.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = new MessageService(simpMessagingTemplate);
    }


    @PostMapping("/api/notify")
    public ResponseEntity<String> sendNotification(@RequestBody ChatNotification notification, @RequestParam String username) {
        messageService.sendUserNotification(username, notification);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PostMapping("/api/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage chatMessage) {
        try {
            Thread.sleep(500);
            var content = chatMessage.content();
            var sender = chatMessage.sender();
            var receiver = chatMessage.receiver();
            if (chatMessage.chatMessageType() == ChatMessageType.USER) {
                logger.info("u/{} -> u/{} : {}", sender, receiver, content);
            } else {
                logger.info("u/{} -> g/{} : {}", sender, receiver, content);
            }
            var message = new ChatMessage(chatMessage.chatMessageType(), content, sender,
                    receiver, LocalDateTime.now().toString());
            if (chatMessage.chatMessageType() == ChatMessageType.USER) {
                messageService.sendUserMessage(receiver, message);
            } else {
                messageService.sendGroupMessage(receiver, message);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

}
