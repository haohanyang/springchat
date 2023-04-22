package haohanyang.springchat.server.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationTokenServiceTest {

    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthenticationTokenServiceTest(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @Test
    void testTokenGeneration() throws Exception {
        var username = "a-user";
        var token = authenticationTokenService.generateToken(username);
        var tokenUsername = authenticationTokenService.verifyToken(token);
        assertEquals(tokenUsername, username);
    }
}