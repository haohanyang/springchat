package haohanyang.springchat.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.server.SpringChatServerApplication;
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
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = SpringChatServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-server.properties")
class UserGroupControllerTest {

    static final ObjectMapper mapper = new ObjectMapper();
    // private String token = null;

    @Autowired
    private MockMvc mvc;

    private static byte[] authenticationBody(String username, String password) throws Exception {
        var form = new AuthenticationRequest(username, password);
        return mapper.writeValueAsBytes(form);
    }

//    @BeforeEach
//    public void authenticateAsUser() throws Exception {
//        var body = authenticationBody("user1", "user1");
//        var loginRequest = MockMvcRequestBuilders.post("/api/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body);
//        var response = mvc.perform(loginRequest).andReturn();
//        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
//        token = authResponse.token();
//    }

    @Test
    void testJoinGroup() throws Exception {
        var body = authenticationBody("user1", "user1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        var token = authResponse.token();

        var req = MockMvcRequestBuilders.
                put("/api/join")
                .header("Authorization", "Bearer " + token)
                .queryParam("initUser", "true")
                .contentType(MediaType.TEXT_PLAIN).content("group1".getBytes());
        mvc.perform(req).andExpect(MockMvcResultMatchers.status().isOk());
    }
}