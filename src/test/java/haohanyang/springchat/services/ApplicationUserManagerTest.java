package haohanyang.springchat.services;

import haohanyang.springchat.identity.ApplicationUserPrincipal;
import haohanyang.springchat.models.User;
import haohanyang.springchat.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationUserManagerTest {

    @Autowired
    private ApplicationUserManager userManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional(readOnly = true)
    void test_user_exists() {
        assertTrue(userManager.userExists("user1"));
        assertTrue(userManager.userExists("user2"));
        assertTrue(userManager.userExists("user3"));
        assertFalse(userManager.userExists("non_existing_user"));
    }

    @Test
    @Transactional(readOnly = true)
    void test_load_existing_user() {
        userManager.loadUserByUsername("user1");
        assertThrows(UsernameNotFoundException.class, () -> userManager.loadUserByUsername("non-existing-user"));
    }

    @Test
    @Transactional
    void test_create_user() {
        userManager.createUser(new ApplicationUserPrincipal(new User("non_existing_user", "password")));
        assertThrows(IllegalArgumentException.class,
                () -> userManager.createUser(new ApplicationUserPrincipal(new User("user1", "password"))));
    }

    @Test
    @Transactional
    void test_delete_user() {
        userManager.deleteUser("user1");
        assertThrows(UsernameNotFoundException.class, () -> userManager.loadUserByUsername("user1"));
        assertThrows(IllegalArgumentException.class, () -> userManager.deleteUser("non_existing_user"));
    }

}