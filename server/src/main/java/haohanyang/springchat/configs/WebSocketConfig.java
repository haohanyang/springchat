package haohanyang.springchat.configs;

import haohanyang.springchat.services.AuthenticationChannelInterceptor;
import haohanyang.springchat.services.AuthenticationTokenService;
import haohanyang.springchat.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserGroupService userGroupService;
    private final AuthenticationTokenService authenticationTokenService;

    public WebSocketConfig(UserGroupService userGroupService, AuthenticationTokenService authenticationTokenService) {
        this.userGroupService = userGroupService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/receive", "/notify");
        // config.setApplicationDestinationPrefixes("/send");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new AuthenticationChannelInterceptor(userGroupService, authenticationTokenService));
    }

}
