package haohanyang.springchat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("server")
public class SpringChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatServerApplication.class, args);
    }

}
