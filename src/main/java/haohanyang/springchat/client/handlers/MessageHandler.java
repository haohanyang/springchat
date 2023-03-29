package haohanyang.springchat.client.handlers;

import haohanyang.springchat.common.Message;
import haohanyang.springchat.common.MessageType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class MessageHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        var message = (Message) payload;
        if (message.messageType() == MessageType.USER) {
            System.out.println("u/" + message.sender() + ":" + message.content());
        } else {
            System.out.println("g/" + message.receiver() + " " + message.sender() + ":" + message.content());
        }

    }
}