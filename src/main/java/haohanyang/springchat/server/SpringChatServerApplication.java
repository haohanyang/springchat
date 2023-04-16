package haohanyang.springchat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "haohanyang.springchat.server")
public class SpringChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatServerApplication.class, args);
    }

}
