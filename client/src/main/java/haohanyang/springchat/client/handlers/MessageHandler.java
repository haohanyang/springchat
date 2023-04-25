package haohanyang.springchat.client.handlers;

import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class MessageHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        var message = (ChatMessage) payload;
        if (message.chatMessageType() == ChatMessageType.USER) {
            System.out.println("u/" + message.sender() + ":" + message.content());
        } else {
            System.out.println("g/" + message.receiver() + " " + message.sender() + ":" + message.content());
        }

    }
}