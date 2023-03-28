package haohanyang.springchat.server.controllers;


import haohanyang.springchat.common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class MessageController {
    Logger logger = LoggerFactory.getLogger(MessageController.class);


    @MessageMapping("/{sender}/{receiver}")
    @SendTo("/receive/{receiver}")
    public Message greeting(@DestinationVariable String sender, @DestinationVariable String receiver,
                            @Payload Message message) throws Exception {
        Thread.sleep(1000); // simulated delay

        logger.info(sender + " -> " + receiver + " " + message);
        return new Message(message.messageType(), message.content(), message.sender(), message.receiver(), LocalDateTime.now().toString());
    }
}
