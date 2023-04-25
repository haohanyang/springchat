package haohanyang.springchat.client.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;


import java.lang.reflect.Type;

public class SessionHandler implements StompSessionHandler {

    private final String token;
    private final String username;

    Logger logger = LoggerFactory.getLogger(SessionHandler.class);

    public SessionHandler(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // Subscribe to receive messages
        var receiveHeaders = new StompHeaders();
        receiveHeaders.add("Authorization", "Bearer " + token);
        receiveHeaders.setDestination("/receive/user/" + username);
        session.subscribe(receiveHeaders, new MessageHandler());

        // Subscribe to receive notifications
        var notificationHeaders = new StompHeaders();
        notificationHeaders.add("Authorization", "Bearer " + token);
        notificationHeaders.setDestination("/notify/user/" + username);
        session.subscribe(notificationHeaders, new NotificationHandler());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Exception {}", exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        logger.error("TransportError {}", exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    }
}