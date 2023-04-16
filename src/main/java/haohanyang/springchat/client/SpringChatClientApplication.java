package haohanyang.springchat.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import static java.lang.System.exit;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = "haohanyang.springchat.client")
public class SpringChatClientApplication implements CommandLineRunner {

    @Autowired
    private Client client;

    public static void main(String[] args) {
        SpringApplication.run(SpringChatClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("springchat");
        client.run();
        exit(0);
    }
}
