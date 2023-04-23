package haohanyang.springchat.server.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-server.properties", properties = {
        "spring.datasource.url=${TEST_DATASOURCE_URL}",
        "spring.datasource.username=${TEST_DATASOURCE_USERNAME}",
        "spring.datasource.password=${TEST_DATASOURCE_PASSWORD}",
})
class UserGroupServiceTest {

    @Autowired
    UserGroupService userGroupService;

    @Test
    void test_valid_add_group() throws Exception {
        userGroupService.addMember("user1", "group4");
    }

    @Test
    void test_non_existing_user_add_group() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.addMember("user_not_existing", "group1"));
    }

    @Test
    void test_user_add_non_existing_group() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> userGroupService.addMember("user1", "group_not_existing"));
    }

}