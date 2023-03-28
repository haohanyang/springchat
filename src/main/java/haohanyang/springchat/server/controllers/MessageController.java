package haohanyang.springchat.server.controllers;


import haohanyang.springchat.common.Message;
import haohanyang.springchat.common.MessageType;
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

import java.time.LocalDateTime;

@Controller
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/user")
    public void userMessage(@Payload Message message) throws Exception {
        Thread.sleep(500);
        var content = message.content();
        var sender = message.sender();
        var receiver = message.receiver();
        logger.info(sender + " -> " + "user/" + receiver + ":" + content);
        var m = new Message(message.messageType(), content, sender,
                receiver, LocalDateTime.now().toString());
        simpMessagingTemplate.convertAndSend("/receive/user/" + receiver, m);
    }

    @MessageMapping("/group")
    public void groupMessage(@Payload Message message) throws Exception {
        Thread.sleep(500);
        var content = message.content();
        var sender = message.sender();
        var receiver = message.receiver();
        logger.info(sender + " -> " + "group/" + receiver + ":" + content);
        var m = new Message(message.messageType(), content, sender,
                receiver, LocalDateTime.now().toString());
        simpMessagingTemplate.convertAndSend("/receive/group/" + receiver, m);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        try {
            Thread.sleep(500);
            var content = message.content();
            var sender = message.sender();
            var receiver = message.receiver();
            logger.info(sender + " -> " + "user/" + receiver + ":" + content);
            var m = new Message(message.messageType(), content, sender,
                    receiver, LocalDateTime.now().toString());
            if (message.messageType() == MessageType.USER) {
                simpMessagingTemplate.convertAndSend("/receive/user/" + receiver, m);
            } else {
                simpMessagingTemplate.convertAndSend("/receive/group/" + receiver, m);
            }
            return ResponseEntity.ok().body("success");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

}
