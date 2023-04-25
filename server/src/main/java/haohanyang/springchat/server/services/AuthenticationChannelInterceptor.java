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

import java.util.regex.Pattern;

public class AuthenticationChannelInterceptor implements ChannelInterceptor {

    private final Pattern userSubscriptionPattern = Pattern.compile("^/receive/user/([a-zA-Z0-9-_]+)$");
    private final Pattern groupSubscriptionPattern = Pattern.compile("^/receive/group/([a-zA-Z0-9-_]+)$");

    Logger logger = LoggerFactory.getLogger(AuthenticationChannelInterceptor.class);

    private final UserGroupService userGroupService;
    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationChannelInterceptor(UserGroupService userGroupService, AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
        this.userGroupService = userGroupService;
    }

    // Make sure a user is authenticated before sending a message
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {

            final String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info("Invalid authorization header format");
                return null;
            }
            var token = authHeader.split(" ")[1].trim();

            String username;
            try {
                username = authenticationTokenService.verifyToken(token);
            } catch (Exception e) {
                logger.info("Invalid token {}...", token.substring(0, 7));
                return null;
            }

            // Check if subscription is valid
            if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
                var destination = accessor.getDestination();
                if (destination == null) {
                    logger.info("Invalid destination:{}", "null");
                    return null;
                }
                // User
                var matcher = userSubscriptionPattern.matcher(destination);
                if (matcher.matches()) {
                    if (!matcher.group(1).equals(username)) {
                        // User can not subscribe to another user's channel
                        logger.info("Invalid destination:{}", accessor.getDestination());
                        return null;
                    }
                    userGroupService.addUser(username);
                    return message;
                }

                // Group
                matcher = groupSubscriptionPattern.matcher(destination);
                if (matcher.matches()) {
                    var groupId = matcher.group(1);
                    if (!userGroupService.userInGroup(username, groupId)) {
                        logger.info("Invalid destination:{}", accessor.getDestination());
                        return null;
                    }
                    return message;
                }

                // Notifications
                return message;
            }

            return message;
        }
        logger.error("StompHeaderAccessor is null");
        return null;
    }
}
