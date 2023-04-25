package haohanyang.springchat.client.configs;

import haohanyang.springchat.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    @Value("${websocket.path}")
    private String webSocketPath;

    @Bean
    public Client getClient() {
        var wsServerUrl = "ws://" + serverAddress + ":" + serverPort + webSocketPath;
        var apiServerUrl = "http://" + serverAddress + ":" + serverPort + "/api";
        return new Client(wsServerUrl, apiServerUrl);
    }

}
