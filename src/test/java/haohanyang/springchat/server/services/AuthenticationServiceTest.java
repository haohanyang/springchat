package haohanyang.springchat.server.services;


import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-server.properties", properties = {
        "spring.datasource.url=${TEST_DATASOURCE_URL}",
        "spring.datasource.username=${TEST_DATASOURCE_USERNAME}",
        "spring.datasource.password=${TEST_DATASOURCE_PASSWORD}",
})
class AuthenticationServiceTest {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Test
    public void testValidRegistration() {
        authenticationService.register("user3", "user3");
    }

    @Test
    public void testInvalidRegistration() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.register("user1", "user1"));
    }

    @Test
    public void testValidLogin() {
        authenticationService.login("user1", "user1");
        authenticationService.login("user2", "user2");
    }

    @Test
    public void testInvalidLogin() {
        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.login("user4", "user4"));
        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.login("user1", "user2"));
    }
}