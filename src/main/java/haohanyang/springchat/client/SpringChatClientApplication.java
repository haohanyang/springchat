package haohanyang.springchat.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Profile;

import java.util.Scanner;

import static java.lang.System.exit;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Profile("client")
public class SpringChatClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length < 1) {
            System.err.println("Please input username");
            System.exit(1);
        }

        try {
            var username = args[0];
            var client = new Client(username);
            client.connect();
            client.subscribe("/receive/user/" + username);
            var scanner = new Scanner(System.in);
            String input;
            while (true) {
                System.out.println("Input message:");
                input = scanner.nextLine();
                if (input.equals("q")) {
                    break;
                }
                System.out.println("To who?");
                var receiver = scanner.nextLine();
                client.send("/send/user", input);
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
        exit(0);
    }
}
