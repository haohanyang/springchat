package haohanyang.springchat.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.common.ChatMessage;
import haohanyang.springchat.common.ChatMessageType;
import haohanyang.springchat.server.SpringChatServerApplication;
import org.junit.After;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
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

    static byte[] authenticationBody(String username, String password) throws Exception {
        var form = new AuthenticationRequest(username, password);
        return mapper.writeValueAsBytes(form);
    }

    @BeforeEach
    public void authenticate() throws Exception {
        var body = authenticationBody("user1", "user1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        token = authResponse.token();
    }

    @Test
    void testUserMessage() throws Exception {
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
    void testGroupMessage() throws Exception {
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
    void testSendNotification() {

    }

}