package haohanyang.springchat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import haohanyang.springchat.client.handlers.MessageHandler;
import haohanyang.springchat.client.handlers.SessionHandler;


public class Client {
    private final WebSocketStompClient stompClient;
    @Nullable
    private String username;
    private StompSession session = null;
    @Nullable
    private String token = null;
    Logger logger = LoggerFactory.getLogger(Client.class);
    private final static String SERVER_URL = "ws://localhost:8080/chat";
    private final ObjectMapper mapper = new ObjectMapper();

    public Client() {
        // Set up stomp client
        var webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        var sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Nullable
    public boolean register(String username, String password) {
        try {
            var form = new AuthenticationRequest(username, password);
            var json = mapper.writeValueAsBytes(form);

            var httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8080/register"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                logger.info("Registration succeeds");
                return true;
            } else {
                logger.error("Registration fails");
            }
        } catch (Exception e) {
            logger.error("Registration fails:" + e.getMessage());
        }
        return false;
    }

    @Nullable
    public boolean login(String username, String password) {
        try {
            var form = new AuthenticationRequest(username, password);
            var json = mapper.writeValueAsBytes(form);

            var httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8080/login"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                var authenticationResponse = mapper.readValue(response.body(), AuthenticationResponse.class);
                this.token = authenticationResponse.token();
                this.username = username;
                logger.info("Login succeeds, try to connect to the websocket server");
                return connect(SERVER_URL);
            } else {
                logger.error("Login fails");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    private boolean connect(String url) {
        try {
            var handshakeHeaders = new WebSocketHttpHeaders();
            var connectionHeaders = new StompHeaders();
            connectionHeaders.add("Authorization", "Bearer " + token);
            var sessionFuture = stompClient.connectAsync(url, handshakeHeaders, connectionHeaders,
                    new SessionHandler(username, token));
            this.session = sessionFuture.get();
            return true;
        } catch (Exception e) {
            logger.error("Connection to " + url + " fails");
            this.session = null;
            this.username = null;
            return false;
        }
    }

    // Subscribe to group messages through websocket
    public boolean subscribe(String groupId) {
        if (session == null || token == null) {
            logger.error("User hasn't logged in");
            return false;
        }
        var header = new StompHeaders();
        header.add("Authorization", "Bearer " + token);
        header.setDestination("/receive/group/" + groupId);
        session.subscribe(header, new MessageHandler());
        return true;
    }


    // Send message through websocket
    public boolean sendStomp(ChatMessageType chatMessageType, String receiver, String content) {
        if (session == null || token == null) {
            logger.error("User hasn't logged in");
            return false;
        }
        var header = new StompHeaders();
        header.add("Authorization", "Bearer " + token);
        if (chatMessageType == ChatMessageType.USER) {
            header.setDestination("/send/user");
        } else {
            header.setDestination("/send/group");
        }
        session.send(header, new ChatMessage(ChatMessageType.USER, content, username, receiver, ""));
        return true;
    }

    // Send message through http post
    public boolean sendPost(ChatMessageType chatMessageType, String receiver, String content) {

        if (session == null || token == null) {
            logger.error("User hasn't logged in");
            return false;
        }

        try {
            var message = new ChatMessage(chatMessageType, content, username, receiver, "");
            var json = mapper.writeValueAsBytes(message);

            var httpRequest = HttpRequest.newBuilder(new URI("http://localhost:8080/send"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                logger.info("Sending message to " + receiver + " succeeds");
                return true;
            } else {
                logger.info("Sending message to " + receiver + " fails");
            }
        } catch (Exception e) {
            logger.info("Sending message to " + receiver + " fails:" + e.getMessage());
        }
        return false;
    }
}