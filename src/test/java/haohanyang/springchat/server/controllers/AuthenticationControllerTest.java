package haohanyang.springchat.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import haohanyang.springchat.common.AuthenticationRequest;
import haohanyang.springchat.common.AuthenticationResponse;
import haohanyang.springchat.server.SpringChatServerApplication;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringChatServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-server.properties", properties = {
        "spring.datasource.url=${TEST_DATASOURCE_URL}",
        "spring.datasource.username=${TEST_DATASOURCE_USERNAME}",
        "spring.datasource.password=${TEST_DATASOURCE_PASSWORD}",
})
class AuthenticationControllerTest {

    static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    static byte[] authenticationBody(String username, String password) throws Exception {
        var form = new AuthenticationRequest(username, password);
        return mapper.writeValueAsBytes(form);
    }

    @Test
    void validLogin() throws Exception {
        var body = authenticationBody("user1", "user1");
        var loginRequest = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        var response = mvc.perform(loginRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        var authResponse = mapper.readValue(response.getResponse().getContentAsByteArray(), AuthenticationResponse.class);
        var token = authResponse.token();

        // Verify the token
        var verifyRequest = MockMvcRequestBuilders.get("/api/verify").queryParam("token", token);
        mvc.perform(verifyRequest).andExpect(MockMvcResultMatchers.status().isOk());

        // Check username
        var checkUsernameRequest = MockMvcRequestBuilders.get("/api/user")
                .header("Authorization", "Bearer " + token);
        mvc.perform(checkUsernameRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("user1"));

    }

    @Test
    void invalidLogin() throws Exception {
        var body = authenticationBody("user4", "user4");
        var request = MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void validRegistration() throws Exception {
        var body = authenticationBody("user3", "user3");
        var request = MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void invalidRegistration() throws Exception {
        var json = authenticationBody("user1", "user1");
        var request = MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}