package haohanyang.springchat.client;

import haohanyang.springchat.common.Message;
import haohanyang.springchat.common.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import haohanyang.springchat.client.handlers.MessageHandler;
import haohanyang.springchat.client.handlers.SessionHandler;


public class Client {
    private final WebSocketStompClient stompClient;
    private final String username;
    private StompSession session = null;

    public Client(String username) {

        this.username = username;
        var webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        var sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public void connect() throws InterruptedException, ExecutionException {
        var url = "ws://localhost:8080/chat";
        var sessionFuture = stompClient.connectAsync(url, new SessionHandler());
        this.session = sessionFuture.get();
    }

    public void subscribe(String path) {
        if (session != null) {
            session.subscribe(path, new MessageHandler());
        }
    }

    public void send(String path, String message) {
        if (session != null) {
            session.send(path, new Message(MessageType.USER, message, username, username, ""));
        }
    }
}