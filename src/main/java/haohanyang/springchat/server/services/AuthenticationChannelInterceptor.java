package haohanyang.springchat.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationChannelInterceptor implements ChannelInterceptor {

    Logger logger = LoggerFactory.getLogger(AuthenticationChannelInterceptor.class);
    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthenticationChannelInterceptor(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    // Make sure a user is authenticated before sending a message
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && (accessor.getCommand() == StompCommand.CONNECT ||
                accessor.getCommand() == StompCommand.SUBSCRIBE)) {
            final String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info(accessor.getCommand() + " fails:" + "invalid token");
                return null;
            }
            var token = authHeader.split(" ")[1].trim();
            if (authenticationTokenService.verifyToken(token) == null) {
                logger.info(accessor.getCommand() + " fails:" + "invalid token");
                return null;
            }
        }
        return message;
    }
}
