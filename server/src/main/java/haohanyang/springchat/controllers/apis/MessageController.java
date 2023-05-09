package haohanyang.springchat.controllers.apis;

import haohanyang.springchat.dtos.MessageDTO;
import haohanyang.springchat.dtos.MessageType;
import haohanyang.springchat.dtos.NotificationDTO;
import haohanyang.springchat.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        // this.messageService = new MessageService(simpMessagingTemplate);
    }

    @PostMapping("/api/notify")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notification,
            @RequestParam String username) {
        messageService.sendUserNotification(username, notification);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PostMapping("/api/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO message) {
        try {
            Thread.sleep(500);
            var content = message.content();
            var sender = message.sender();
            var receiver = message.receiver();
            if (message.messageType() == MessageType.USER) {
                messageService.sendUserMessage(message);
            } else {
                messageService.sendGroupMessage(message);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

}
