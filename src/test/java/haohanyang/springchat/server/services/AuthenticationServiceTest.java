package haohanyang.springchat.server.services;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


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

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void test_valid_registration() {
        authenticationService.register("user_not_exists", "password");
    }

    @Test
    @Transactional
    public void test_invalid_registration() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.register("user1", "user1"));
    }

    @Test
    @Transactional(readOnly = true)
    public void test_valid_login() throws Exception {
        authenticationService.login("user1", "password1");
    }

    @Test
    @Transactional(readOnly = true)
    public void test_invalid_login() {
        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.login("user_not_exists", "user4"));
        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.login("user1", "wrong_password"));
    }
}