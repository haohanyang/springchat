package haohanyang.springchat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.client.cmd.*;
import haohanyang.springchat.client.handlers.NotificationHandler;
import haohanyang.springchat.common.*;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.io.ByteArrayInputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import haohanyang.springchat.client.handlers.MessageHandler;
import haohanyang.springchat.client.handlers.SessionHandler;

public class Client {

    private final String wsServerUrl;
    private final String apiServerUrl;

    @Nullable
    private final WebSocketStompClient stompClient;
    @Nullable
    private String username;
    @Nullable
    private StompSession session = null;
    @Nullable
    private String token = null;
    Logger logger = LoggerFactory.getLogger(Client.class);

    private final ObjectMapper mapper = new ObjectMapper();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public Client(String wsServerUrl, String apiServerUrl) {
        // Server url
        this.wsServerUrl = wsServerUrl;
        this.apiServerUrl = apiServerUrl;

        // Set up stomp client
        var webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        var sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private void register(String username, String password) {
        try {
            var form = new AuthenticationRequest(username, password);
            var json = mapper.writeValueAsBytes(form);

            var httpRequest = HttpRequest.newBuilder(new URI(apiServerUrl + "/register"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            var response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                printSucceed("ok");
            } else {
                printError("error:" + response.body());
            }
        } catch (Exception e) {
            printError("error:" + e.getMessage());
        }
    }


    private void login(String username, String password) {
        try {
            var form = new AuthenticationRequest(username, password);
            var json = mapper.writeValueAsBytes(form);

            var httpRequest = HttpRequest.newBuilder(new URI(apiServerUrl + "/login"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            var response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                var authenticationResponse = mapper.readValue(response.body(), AuthenticationResponse.class);
                this.token = authenticationResponse.token();
                this.username = username;

                // Connect to the stomp server
                var handshakeHeaders = new WebSocketHttpHeaders();
                var connectionHeaders = new StompHeaders();
                connectionHeaders.add("Authorization", "Bearer " + token);
                var sessionFuture = stompClient.connectAsync(wsServerUrl, handshakeHeaders, connectionHeaders,
                        new SessionHandler(username, token));
                this.session = sessionFuture.get();
                printSucceed("ok");
            } else {
                printError("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            printError("Failed to connect to the server");
        }
    }

    private void printResponse(ChatNotification notification) {
        var message = notification.message();
        switch (notification.type()) {
            case WARNING -> printWarning("warning:" + message);
            case ERROR -> printError("error:" + message);
            default -> printSucceed("ok");
        }
    }

    private StompHeaders getStompHeaders() {
        var headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    private void printError(String message) {
        System.err.println(message);
    }

    private void printWarning(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    private void printSucceed(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    // Subscribe to group messages through websocket
    private void join(String groupId) {
        if (session == null || token == null) {
            logger.error("join({}) error:{}", groupId, "User hasn't logged in");
            printError("error:" + "You haven't logged in");
            return;
        }
        try {
            var httpRequest = HttpRequest.newBuilder(new URI(apiServerUrl + "/join"))
                    .header("Content-Type", "text/plain;charset=utf-8")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(groupId))
                    .build();

            var response =
                    HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var notification = mapper.readValue(response.body(), ChatNotification.class);
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                var receiveHeaders = getStompHeaders();
                receiveHeaders.setDestination("/receive/group/" + groupId);
                session.subscribe(receiveHeaders, new MessageHandler());

                var notificationHeaders = getStompHeaders();
                notificationHeaders.setDestination("/notify/group/" + groupId);
                session.subscribe(notificationHeaders, new NotificationHandler());
                printResponse(notification);
            } else {
                logger.error("join({}) error:{}", groupId, notification.message());
                printResponse(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Send message through websocket
    @Deprecated
    private boolean sendStomp(ChatMessageType chatMessageType, String receiver, String content) {
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
        session.send(header, new ChatMessage(ChatMessageType.USER, content, username, receiver));
        return true;
    }

    // Send message through http post
    private void send(ChatMessageType chatMessageType, String receiver, String content) {

        if (session == null || token == null) {
            logger.error("send({},{},{}) error:{}", chatMessageType, receiver, content, "User hasn't logged in");
            printError("error:" + "You haven't logged in");
            return;
        }

        var message = new ChatMessage(chatMessageType, content, username, receiver);
        try {
            var json = mapper.writeValueAsBytes(message);

            var httpRequest = HttpRequest.newBuilder(new URI(apiServerUrl + "/send"))
                    .header("Content-Type", "application/json;charset=utf-8")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(json)))
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                printSucceed("ok");
            } else {
                printError("error:" + response.body());
            }
        } catch (Exception e) {
            printError("error:" + e.getMessage());
        }

    }

    public void run() {

        while (true) {
            var scanner = new Scanner(System.in);
            var commandString = scanner.nextLine();
            var command = CommandlineParser.parse(commandString);

            if (command == null) {
                System.err.println("Invalid input");
                continue;
            }
            if (command instanceof ExitCommand) {
                break;
            }

            if (command instanceof RegistrationCommand registrationCommand) {
                register(registrationCommand.username(), registrationCommand.password());
            }

            if (command instanceof LoginCommand loginCommand) {
                login(loginCommand.username(), loginCommand.password());
            }

            if (command instanceof SendCommand sendCommand) {
                send(sendCommand.chatMessageType(), sendCommand.receiver(), sendCommand.content());
            }

            if (command instanceof JoinGroupCommand joinGroupCommand) {
                join(joinGroupCommand.groupId());
            }
        }
    }
}