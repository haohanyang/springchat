package haohanyang.springchat.server.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    Logger logger = LoggerFactory.getLogger(MessageController.class);


    @MessageMapping("/{sender}/{receiver}")
    @SendTo("/receive/{receiver}")
    public String greeting(@DestinationVariable String sender, @DestinationVariable String receiver,
                           @Payload String message) throws Exception {
        Thread.sleep(1000); // simulated delay

        logger.info(sender + " -> " + receiver + " " + message);
        return sender + ":" + message;
    }
}
