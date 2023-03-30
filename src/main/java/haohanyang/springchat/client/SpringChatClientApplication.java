package haohanyang.springchat.client;

import haohanyang.springchat.client.cmd.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Scanner;

import static java.lang.System.exit;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringChatClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("springchat");
        var client = new Client();

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
                if (!client.register(registrationCommand.username(), registrationCommand.password())) {
                    System.err.println("Registration fails");
                } else {
                    System.out.println("Registration succeeds");
                }
            }

            if (command instanceof LoginCommand loginCommand) {
                if (!client.login(loginCommand.username(), loginCommand.password())) {
                    System.err.println("Login fails");
                } else {
                    System.out.println("Login succeeds");
                }
            }

            if (command instanceof SendCommand sendCommand) {
                if (!client.sendPost(sendCommand.chatMessageType(), sendCommand.receiver(), sendCommand.content())) {
                    System.err.println("Message delivery fails");
                }
            }

            if (command instanceof JoinGroupCommand joinGroupCommand) {
                if (!client.join(joinGroupCommand.groupId())) {
                    System.err.println("Error");
                }
            }
        }
        exit(0);
    }
}
