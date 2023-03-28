package haohanyang.springchat;

import haohanyang.springchat.client.SpringChatClientApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ClientLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SpringChatClientApplication.class).
                web(WebApplicationType.NONE).profiles("client").run(args);
    }
}
