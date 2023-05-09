package haohanyang.springchat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.SpringChatApplication;
import haohanyang.springchat.dtos.AuthenticationRequest;
import haohanyang.springchat.dtos.LoginResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringChatApplication.class)
@AutoConfigureMockMvc
class UserGroupControllerTest {

    static final ObjectMapper mapper = new ObjectMapper();
    private String token = null;

    @Autowired
    private MockMvc mvc;

    private static byte[] authenticationBody(String username, String password) throws Exception {
        var form = new AuthenticationRequest(username, password);
        return mapper.writeValueAsBytes(form);
    }

    @Test
    @BeforeAll
    public void authenticate() throws Exception {
        var body = authenticationBody("user1", "password1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        response.getResponse();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(),
                LoginResponse.class);
        token = authResponse.token();
    }

    @Test
    void test_join_group() throws Exception {
        assertNotNull(token);
        var body = authenticationBody("user1", "password1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(),
                LoginResponse.class);
        var token = authResponse.token();

        var req = MockMvcRequestBuilders.put("/api/join")
                .header("Authorization", "Bearer " + token)
                .queryParam("initUser", "true")
                .contentType(MediaType.TEXT_PLAIN).content("group4".getBytes());
        mvc.perform(req).andExpect(MockMvcResultMatchers.status().isCreated());
    }
}