package haohanyang.springchat.client.handlers;

import haohanyang.springchat.common.ChatNotification;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class NotificationHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatNotification.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        var notification = (ChatNotification) payload;
        System.out.println("Info:" + notification.message());
    }
}
