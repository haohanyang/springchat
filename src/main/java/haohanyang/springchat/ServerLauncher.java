package haohanyang.springchat;

import haohanyang.springchat.server.SpringChatServerApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ServerLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringChatServerApplication.class).profiles("server").run();
    }
}
