package haohanyang.springchat.server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Test
    public void contextLoads() throws Exception {

    }
}