package haohanyang.springchat.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.common.*;
import haohanyang.springchat.server.SpringChatServerApplication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = SpringChatServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-server.properties")
class MessageControllerTest {

    static final ObjectMapper mapper = new ObjectMapper();
    private String token = null;
    @Autowired
    private MockMvc mvc;

    private static byte[] authenticationBody(String username, String password) throws Exception {
        var form = new AuthenticationRequest(username, password);
        return mapper.writeValueAsBytes(form);
    }

    @BeforeEach
    public void authenticateAsUser() throws Exception {
        var body = authenticationBody("user1", "user1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        token = authResponse.token();
    }

    @AfterEach
    public void clearToken() {
        token = null;
    }

    private void authenticateAsAdmin() throws Exception {
        var body = authenticationBody("admin", "admin");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        token = authResponse.token();
    }

    @Test
    public void testUserMessage() throws Exception {
        assertNotNull(token);
        var message = new ChatMessage(ChatMessageType.USER, "test message", "user1", "user2", "");
        var body = mapper.writeValueAsBytes(message);
        var sendMessageRequest = MockMvcRequestBuilders.post("/api/send")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mvc.perform(sendMessageRequest).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGroupMessage() throws Exception {
        assertNotNull(token);
        var message = new ChatMessage(ChatMessageType.GROUP, "test message", "user1", "group1", "");
        var body = mapper.writeValueAsBytes(message);
        var sendMessageRequest = MockMvcRequestBuilders.post("/api/send")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mvc.perform(sendMessageRequest).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testSendNotification() throws Exception {
        var message = new ChatNotification(ChatNotificationType.INFO, "test info");
        var body = mapper.writeValueAsBytes(message);
        var userSendMessageRequest = MockMvcRequestBuilders.post("/api/notify")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .queryParam("username", "user1");
        mvc.perform(userSendMessageRequest).andExpect(MockMvcResultMatchers.status().isForbidden());

        token = null;
        authenticateAsAdmin();
        assertNotNull(token);
        var adminSendMessageRequest = MockMvcRequestBuilders.post("/api/notify")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .queryParam("username", "user1");
        mvc.perform(adminSendMessageRequest).andExpect(MockMvcResultMatchers.status().isCreated());

    }
}